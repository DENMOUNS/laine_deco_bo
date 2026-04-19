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
    String currentTier,
    String tierDescription,
    Instant tierReachedAt,
    BigDecimal totalSpent,
    Integer orderCount,
    Instant createdAt
) {}
