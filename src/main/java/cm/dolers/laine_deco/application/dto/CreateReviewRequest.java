package cm.dolers.laine_deco.application.dto;

/**
 * DTO pour créer un avis
 */
public record CreateReviewRequest(
    Long productId,
    Integer rating,
    String title,
    String comment
) {}
