package cm.dolers.laine_deco.application.usecase.impl;

import cm.dolers.laine_deco.application.dto.UserLoyaltyResponse;
import cm.dolers.laine_deco.application.dto.LoyaltyRedemptionResponse;
import cm.dolers.laine_deco.application.dto.RedeemRewardRequest;
import cm.dolers.laine_deco.application.mapper.LoyaltyMapper;
import cm.dolers.laine_deco.application.usecase.LoyaltyService;
import cm.dolers.laine_deco.application.usecase.BadgeService;
import cm.dolers.laine_deco.domain.exception.ErrorCode;
import cm.dolers.laine_deco.domain.exception.ValidationException;
import cm.dolers.laine_deco.domain.model.LoyaltyTier;
import cm.dolers.laine_deco.domain.model.RewardType;
import cm.dolers.laine_deco.infrastructure.persistence.entity.UserLoyaltyProfile;
import cm.dolers.laine_deco.infrastructure.persistence.entity.LoyaltyRedemption;
import cm.dolers.laine_deco.infrastructure.persistence.repository.UserLoyaltyProfileRepository;
import cm.dolers.laine_deco.infrastructure.persistence.repository.UserJpaRepository;
import cm.dolers.laine_deco.infrastructure.persistence.repository.LoyaltyRedemptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoyaltyServiceImpl implements LoyaltyService {
    private final UserLoyaltyProfileRepository loyaltyRepository;
    private final UserJpaRepository userRepository;
    private final LoyaltyRedemptionRepository redemptionRepository;
    private final LoyaltyMapper loyaltyMapper;
    private final BadgeService badgeService;

    @Override
    @Transactional
    public void addPointsFromOrder(Long userId, BigDecimal orderAmount) {
        log.info("Adding loyalty points for user: {}, amount: {}", userId, orderAmount);

        var user = userRepository.findById(userId)
            .orElseThrow(() -> new ValidationException(ErrorCode.USER_NOT_FOUND, "User ID: " + userId));

        // Récupérer ou créer le profil de loyauté
        var profile = loyaltyRepository.findByUserId(userId)
            .orElseGet(() -> {
                var newProfile = new UserLoyaltyProfile();
                newProfile.setUser(user);
                newProfile.setCurrentTier(LoyaltyTier.STANDARD);
                return newProfile;
            });

        // Calculer les points à ajouter
        // Règle: 10 points de base, 15 si montant > 100.000
        int pointsToAdd = 10;
        if (orderAmount.compareTo(BigDecimal.valueOf(100000)) > 0) {
            pointsToAdd = 15;
        }

        // Ajouter les points
        int previousTotal = profile.getTotalPoints();
        profile.setTotalPoints(profile.getTotalPoints() + pointsToAdd);
        profile.setAvailablePoints(profile.getAvailablePoints() + pointsToAdd);
        profile.setTotalSpent(profile.getTotalSpent().add(orderAmount));
        profile.setOrderCount(profile.getOrderCount() + 1);
        profile.setUpdatedAt(Instant.now());

        // Vérifier le changement de tier
        LoyaltyTier oldTier = profile.getCurrentTier();
        LoyaltyTier newTier = LoyaltyTier.fromPoints(profile.getTotalPoints());

        if (!oldTier.equals(newTier)) {
            profile.setCurrentTier(newTier);
            profile.setTierReachedAt(Instant.now());
            log.info("User {} promoted from {} to {}", userId, oldTier, newTier);
        }

        // Sauvegarder
        loyaltyRepository.save(profile);

        // Attribuer les badges automatiquement
        badgeService.checkAndAwardAutomatic(userId);

        log.info("Points added: {} (was {}, now {})", pointsToAdd, previousTotal, profile.getTotalPoints());
    }

    @Override
    @Transactional(readOnly = true)
    public UserLoyaltyResponse getUserLoyalty(Long userId) {
        var profile = loyaltyRepository.findByUserId(userId)
            .orElseThrow(() -> new ValidationException(ErrorCode.USER_NOT_FOUND, "User loyalty not found: " + userId));
        return loyaltyMapper.toResponse(profile);
    }

    @Override
    @Transactional
    public LoyaltyRedemptionResponse redeemReward(Long userId, RedeemRewardRequest request) {
        log.info("Redeeming reward for user: {}, type: {}", userId, request.rewardType());

        var profile = loyaltyRepository.findByUserId(userId)
            .orElseThrow(() -> new ValidationException(ErrorCode.USER_NOT_FOUND, "User loyalty not found: " + userId));

        // Déterminer le type de récompense
        RewardType rewardType;
        try {
            rewardType = RewardType.valueOf(request.rewardType());
        } catch (IllegalArgumentException ex) {
            throw new ValidationException(ErrorCode.OPERATION_FAILED, "Invalid reward type: " + request.rewardType());
        }

        // Vérifier que l'utilisateur a assez de points
        if (profile.getAvailablePoints() < rewardType.getPointsRequired()) {
            throw new ValidationException(ErrorCode.OPERATION_FAILED,
                "Insufficient points: have " + profile.getAvailablePoints() + ", need " + rewardType.getPointsRequired());
        }

        // Créer la rédemption
        var redemption = new LoyaltyRedemption();
        redemption.setUser(profile.getUser());
        redemption.setRewardType(rewardType);
        redemption.setPointsUsed(rewardType.getPointsRequired());
        redemption.setRewardValue(rewardType.getValue());
        redemption.setStatus(LoyaltyRedemption.RedemptionStatus.PENDING);
        redemption.setNotes("Redemption created");

        // Déduire les points disponibles
        profile.setAvailablePoints(profile.getAvailablePoints() - rewardType.getPointsRequired());
        profile.setUpdatedAt(Instant.now());

        // Sauvegarder
        var saved = redemptionRepository.save(redemption);
        loyaltyRepository.save(profile);

        log.info("Reward redeemed: id={}, type={}, points={}", saved.getId(), rewardType, rewardType.getPointsRequired());
        return loyaltyMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoyaltyRedemptionResponse> getUserRedemptions(Long userId, Pageable pageable) {
        return redemptionRepository.findByUserId(userId, pageable)
            .map(loyaltyMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoyaltyRedemptionResponse> getPendingRedemptions(Long userId, Pageable pageable) {
        return redemptionRepository.findByUserIdAndStatus(userId, LoyaltyRedemption.RedemptionStatus.PENDING, pageable)
            .map(loyaltyMapper::toResponse);
    }
}
