package cm.dolers.laine_deco.application.dto;

import java.time.Instant;

/**
 * Record DTO pour créer un avis
 */
public record CreateReviewRequest(
    Long productId,
    Integer rating,
    String title,
    String comment
) {}

/**
 * Record DTO pour la réponse d'avis
 */
public record ReviewResponse(
    Long id,
    Long productId,
    Long userId,
    String userName,
    Integer rating,
    String title,
    String comment,
    String status,
    Integer helpfulCount,
    Instant createdAt
) {}
