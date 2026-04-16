package cm.dolers.laine_deco.application.dto;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO pour le profil de loyauté d'un utilisateur
 */
public record UserLoyaltyResponse(
    Long userId,
    Integer totalPoints,
    Integer availablePoints,
    String currentTier,                      // STANDARD, PREMIUM, VIP, GOLD
    String tierDescription,
    Instant tierReachedAt,
    BigDecimal totalSpent,
    Integer orderCount,
    Instant createdAt
) {}

/**
 * DTO pour badge gagné
 */
public record UserBadgeResponse(
    Long id,
    String badgeType,                        // FIRST_ORDER, PREMIUM_REACHED, etc.
    String label,
    String icon,
    String description,
    Instant earnedAt
) {}

/**
 * DTO pour une rédemption
 */
public record LoyaltyRedemptionResponse(
    Long id,
    String rewardType,
    String rewardLabel,
    Integer pointsUsed,
    BigDecimal rewardValue,
    String status,                           // PENDING, APPLIED, EXPIRED
    Instant appliedAt,
    Instant expiresAt,
    String notes,
    Instant createdAt
) {}

/**
 * Request pour redémer une récompense
 */
public record RedeemRewardRequest(
    String rewardType                        // FREE_SHIPPING, COUPON_10K, PREMIUM_REWARD
) {}
