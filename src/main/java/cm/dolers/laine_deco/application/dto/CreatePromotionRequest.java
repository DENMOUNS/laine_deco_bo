package cm.dolers.laine_deco.application.dto;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO pour créer une promotion
 */
public record CreatePromotionRequest(
    String name,
    String description,
    String type, // FLASH_SALE, CATEGORY_DISCOUNT, BRAND_DISCOUNT, SEASONAL_SALE
    Instant startDate,
    Instant endDate,
    BigDecimal discountPercentage,  // 0-100
    BigDecimal discountAmount,      // montant fixe ou prix réduit
    Long categoryId,                // null si pas de réduction par catégorie
    String brand,                   // null si pas de réduction par marque
    Long productId                  // null si pas de vente flash sur produit
) {}
