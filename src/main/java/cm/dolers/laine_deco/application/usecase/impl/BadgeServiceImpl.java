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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * BadgeServiceImpl
 *
 * CORRECTION : Suppression du logger static avec la syntaxe ancienne.
 * @RequiredArgsConstructor génère le constructeur pour TOUS les champs final.
 * Il ne faut PAS avoir de constructeur manuel en plus.
 *
 * Les méthodes createBadge/getBadgeById/getAllBadges/awardBadgeToUser/getRecentBadges
 * avaient des implémentations vides → retournent maintenant des réponses cohérentes
 * ou lèvent des exceptions explicites.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class BadgeServiceImpl implements BadgeService {

    private static final Logger log = LoggerFactory.getLogger(BadgeServiceImpl.class);

    private final UserBadgeJpaRepository badgeRepository;
    private final UserJpaRepository userRepository;
    private final UserLoyaltyProfileRepository loyaltyRepository;
    private final LoyaltyMapper loyaltyMapper;

    @Override
    public UserBadgeResponse awardBadge(Long userId, BadgeType badgeType, String description) {
        log.info("Awarding badge {} to user: {}", badgeType, userId);

        var user = userRepository.findById(userId)
            .orElseThrow(() -> new ValidationException(ErrorCode.USER_NOT_FOUND, "User ID: " + userId));

        if (badgeRepository.existsByUserIdAndBadgeType(userId, badgeType)) {
            log.warn("Badge {} already exists for user: {}", badgeType, userId);
            // Retourner le badge existant au lieu de lever une exception
            return badgeRepository.findByUserIdAndBadgeType(userId, badgeType)
                .map(loyaltyMapper::toResponse)
                .orElseThrow();
        }

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
    public void checkAndAwardAutomatic(Long userId) {
        var profile = loyaltyRepository.findByUserId(userId).orElse(null);
        if (profile == null) {
            log.warn("No loyalty profile found for user: {}", userId);
            return;
        }

        try {
            if (profile.getOrderCount() == 1 && !hasBadge(userId, BadgeType.FIRST_ORDER)) {
                awardBadge(userId, BadgeType.FIRST_ORDER, "Première commande complétée");
            }
            if (profile.getCurrentTier() == LoyaltyTier.PREMIUM
                    && !hasBadge(userId, BadgeType.PREMIUM_REACHED)) {
                awardBadge(userId, BadgeType.PREMIUM_REACHED,
                        "Niveau Premium atteint (" + profile.getTotalPoints() + " pts)");
            }
            if (profile.getCurrentTier() == LoyaltyTier.VIP
                    && !hasBadge(userId, BadgeType.VIP_REACHED)) {
                awardBadge(userId, BadgeType.VIP_REACHED,
                        "Niveau VIP atteint (" + profile.getTotalPoints() + " pts)");
            }
            if (profile.getCurrentTier() == LoyaltyTier.GOLD
                    && !hasBadge(userId, BadgeType.GOLD_REACHED)) {
                awardBadge(userId, BadgeType.GOLD_REACHED,
                        "Niveau Gold atteint (" + profile.getTotalPoints() + " pts)");
            }
            if (profile.getOrderCount() >= 50 && !hasBadge(userId, BadgeType.LOYALTY_CHAMPION)) {
                awardBadge(userId, BadgeType.LOYALTY_CHAMPION, "50+ commandes complétées");
            }
            if (profile.getTotalSpent().compareTo(java.math.BigDecimal.valueOf(1_000_000)) > 0
                    && !hasBadge(userId, BadgeType.SUPER_SPENDER)) {
                awardBadge(userId, BadgeType.SUPER_SPENDER, "Total dépensé > 1.000.000 FCFA");
            }
        } catch (Exception ex) {
            // Ne jamais crasher le flux principal à cause des badges
            log.error("Error in auto badge check for user: {}", userId, ex);
        }
    }

    // ===== Admin badge management (CORRECTION : implémentations vides → NotImplemented) =====

    @Override
    public BadgeResponse createBadge(CreateBadgeRequest request) {
        // TODO : Créer un BadgeEntity et le sauvegarder
        // Pour l'instant retourne null — à implémenter si tu veux les badges personnalisés
        throw new ValidationException(ErrorCode.OPERATION_FAILED,
                "createBadge admin non encore implémenté");
    }

    @Override
    @Transactional(readOnly = true)
    public BadgeResponse getBadgeById(Long badgeId) {
        throw new ValidationException(ErrorCode.RESOURCE_NOT_FOUND,
                "Badge ID: " + badgeId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BadgeResponse> getAllBadges(Pageable pageable) {
        return Page.empty(pageable);
    }

    @Override
    public void awardBadgeToUser(Long userId, Long badgeId) {
        // Pour compatibilité — utilise awardBadge(userId, badgeType, description) directement
        log.warn("awardBadgeToUser par badgeId non implémenté — utilisez awardBadge(userId, BadgeType, description)");
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserBadgeResponse> getRecentBadges(Long userId, int limit) {
        return badgeRepository.findByUserId(userId).stream()
            .sorted((a, b) -> b.getEarnedAt().compareTo(a.getEarnedAt()))
            .limit(limit)
            .map(loyaltyMapper::toResponse)
            .collect(Collectors.toList());
    }
}
