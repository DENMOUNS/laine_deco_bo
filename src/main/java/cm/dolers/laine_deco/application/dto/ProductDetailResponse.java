package cm.dolers.laine_deco.application.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Record DTO pour la réponse détaillée d'un produit
 */
public record ProductDetailResponse(
    Long id,
    String sku,
    String name,
    String description,
    BigDecimal salePrice,
    BigDecimal costPrice,
    BigDecimal margin,
    BigDecimal marginPercentage,
    Integer stockQuantity,
    Integer reorderLevel,
    String material,
    List<String> colors,
    String brand,
    Double rating,
    Boolean isNew,
    Boolean isSale,
    Boolean isAvailable,
    String warranty,
    Boolean isElectronic,
    Integer viewsCount,
    Integer salesCount,
    Long categoryId,
    Instant createdAt,
    Instant updatedAt
) {}
