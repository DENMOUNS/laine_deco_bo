package cm.dolers.laine_deco.application.dto;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO pour une rédemption
 */
public record LoyaltyRedemptionResponse(
    Long id,
    String rewardType,
    String rewardLabel,
    Integer pointsUsed,
    BigDecimal rewardValue,
    String status,
    Instant appliedAt,
    Instant expiresAt,
    String notes,
    Instant createdAt
) {}
