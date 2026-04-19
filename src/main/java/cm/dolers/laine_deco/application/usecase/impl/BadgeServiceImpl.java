package cm.dolers.laine_deco.application.usecase.impl;

import cm.dolers.laine_deco.application.dto.*;

import cm.dolers.laine_deco.application.mapper.LoyaltyMapper;
import cm.dolers.laine_deco.application.usecase.BadgeService;
import cm.dolers.laine_deco.domain.exception.ErrorCode;
import cm.dolers.laine_deco.domain.exception.ValidationException;
import cm.dolers.laine_deco.domain.model.BadgeType;
import cm.dolers.laine_deco.domain.model.LoyaltyTier;
import cm.dolers.laine_deco.infrastructure.persistence.entity.UserBadgeEntity;
import cm.dolers.laine_deco.infrastructure.persistence.repository.UserBadgeJpaRepository;
import cm.dolers.laine_deco.infrastructure.persistence.repository.UserJpaRepository;
import cm.dolers.laine_deco.infrastructure.persistence.repository.UserLoyaltyProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BadgeServiceImpl implements BadgeService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BadgeServiceImpl.class);
    private final UserBadgeJpaRepository badgeRepository;
    private final UserJpaRepository userRepository;
    private final UserLoyaltyProfileRepository loyaltyRepository;
    private final LoyaltyMapper loyaltyMapper;

    @Override
    @Transactional
    public UserBadgeResponse awardBadge(Long userId, BadgeType badgeType, String description) {
        log.info("Awarding badge {} to user: {}", badgeType, userId);

        var user = userRepository.findById(userId)
            .orElseThrow(() -> new ValidationException(ErrorCode.USER_NOT_FOUND, "User ID: " + userId));

        // Vérifier si le badge existe déjà
        if (badgeRepository.existsByUserIdAndBadgeType(userId, badgeType)) {
            log.warn("Badge {} already exists for user: {}", badgeType, userId);
            throw new ValidationException(ErrorCode.OPERATION_FAILED, "Badge already owned: " + badgeType);
        }

        // Créer le badge
        var badge = new UserBadgeEntity();
        badge.setUser(user);
        badge.setBadgeType(badgeType);
        badge.setEarnedAt(Instant.now());
        badge.setDescription(description);

        var saved = badgeRepository.save(badge);
        log.info("Badge awarded: id={}, type={}", saved.getId(), badgeType);
        return loyaltyMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserBadgeResponse> getUserBadges(Long userId) {
        return badgeRepository.findByUserId(userId).stream()
            .map(loyaltyMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserBadgeResponse> getUserBadgesPaginated(Long userId, Pageable pageable) {
        return badgeRepository.findByUserId(userId, pageable)
            .map(loyaltyMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasBadge(Long userId, BadgeType badgeType) {
        return badgeRepository.existsByUserIdAndBadgeType(userId, badgeType);
    }

    @Override
    @Transactional
    public void checkAndAwardAutomatic(Long userId) {
        var profile = loyaltyRepository.findByUserId(userId)
            .orElse(null);

        if (profile == null) {
            log.warn("No loyalty profile found for user: {}", userId);
            return;
        }

        try {
            // Badge: Première Commande
            if (profile.getOrderCount() == 1 && !hasBadge(userId, BadgeType.FIRST_ORDER)) {
                awardBadge(userId, BadgeType.FIRST_ORDER, "Première commande complétée");
            }

            // Badge: Premium atteint
            if (profile.getCurrentTier() == LoyaltyTier.PREMIUM &&
                !hasBadge(userId, BadgeType.PREMIUM_REACHED)) {
                awardBadge(userId, BadgeType.PREMIUM_REACHED,
                    "Nouveau level atteint: Premium (" + profile.getTotalPoints() + " points)");
            }

            // Badge: VIP atteint
            if (profile.getCurrentTier() == LoyaltyTier.VIP &&
                !hasBadge(userId, BadgeType.VIP_REACHED)) {
                awardBadge(userId, BadgeType.VIP_REACHED,
                    "Nouveau level atteint: VIP (" + profile.getTotalPoints() + " points)");
            }

            // Badge: Gold atteint
            if (profile.getCurrentTier() == LoyaltyTier.GOLD &&
                !hasBadge(userId, BadgeType.GOLD_REACHED)) {
                awardBadge(userId, BadgeType.GOLD_REACHED,
                    "Nouveau level atteint: Gold (" + profile.getTotalPoints() + " points)");
            }

            // Badge: Champion Fidélité (50+ commandes)
            if (profile.getOrderCount() >= 50 && !hasBadge(userId, BadgeType.LOYALTY_CHAMPION)) {
                awardBadge(userId, BadgeType.LOYALTY_CHAMPION,
                    "50+ commandes complétées");
            }

            // Badge: Super Dépensier (> 1.000.000 dépensés)
            if (profile.getTotalSpent().compareTo(java.math.BigDecimal.valueOf(1000000)) > 0 &&
                !hasBadge(userId, BadgeType.SUPER_SPENDER)) {
                awardBadge(userId, BadgeType.SUPER_SPENDER,
                    "Total dépensé > 1.000.000 FCFA");
            }
        } catch (ValidationException ex) {
            // Badge déjà existant, ignorer
            log.debug("Badge already awarded for user: {}", userId);
        } catch (Exception ex) {
            log.error("Error awarding automatic badges for user: {}", userId, ex);
        }
    }


    @Override
    @Transactional
    public BadgeResponse createBadge(CreateBadgeRequest request) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public BadgeResponse getBadgeById(Long badgeId) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BadgeResponse> getAllBadges(Pageable pageable) {
        return Page.empty();
    }

    @Override
    @Transactional
    public void awardBadgeToUser(Long userId, Long badgeId) {
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserBadgeResponse> getRecentBadges(Long userId, int limit) {
        return List.of();
    }
}

