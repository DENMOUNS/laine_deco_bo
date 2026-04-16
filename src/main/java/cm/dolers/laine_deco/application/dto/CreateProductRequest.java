package cm.dolers.laine_deco.application.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Record DTO pour créer/modifier un produit
 */
public record CreateProductRequest(
    String sku,
    String name,
    String description,
    BigDecimal salePrice,
    BigDecimal costPrice,
    Integer stockQuantity,
    Integer reorderLevel,
    String material,
    List<String> colors,
    String brand,
    String warranty,
    Boolean isElectronic,
    Long categoryId
) {}
