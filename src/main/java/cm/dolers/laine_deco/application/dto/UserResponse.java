package cm.dolers.laine_deco.application.dto;

import java.time.Instant;

/**
 * Record DTO pour la réponse utilisateur
 */
public record UserResponse(
    Long id,
    String email,
    String name,
    String phone,
    String loyaltyTier,
    Integer points,
    String preferredLanguage,
    String preferredTheme,
    Instant createdAt
) {}
