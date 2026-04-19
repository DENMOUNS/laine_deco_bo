package cm.dolers.laine_deco.application.dto;


/**
 * DTO pour marque avec comptage de produits
 */
public record BrandWithCountResponse(
    String name,
    Long productCount
) {}

