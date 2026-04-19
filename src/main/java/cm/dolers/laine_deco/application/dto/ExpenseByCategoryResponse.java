package cm.dolers.laine_deco.application.dto;


import java.math.BigDecimal;

/**
 * DTO pour les dépenses par catégorie
 */
public record ExpenseByCategoryResponse(
    String categoryName,
    BigDecimal totalAmount,
    Integer count,
    Double percentageOfTotal
) {}
