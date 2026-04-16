package cm.dolers.laine_deco.application.dto;

import java.time.Instant;

/**
 * Record DTO pour les items de wishlist
 */
public record WishlistItemResponse(
    Long id,
    Long productId,
    String productName,
    String productImage,
    String productSku,
    Instant addedAt
) {}
