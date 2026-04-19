package cm.dolers.laine_deco.application.dto;

import java.math.BigDecimal;

/**
 * DTO pour les revenus par catégorie
 */
public record CategoryRevenueResponse(
    Long categoryId,
    String categoryName,
    Long productCount,
    BigDecimal revenue,
    Integer salesCount,
    Double percentageOfTotal
) {}
