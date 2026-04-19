package cm.dolers.laine_deco.application.dto;


/**
 * DTO pour catégorie avec comptage de produits
 */
public record CategoryWithCountResponse(
    Long id,
    String name,
    String description,
    Long productCount,
    String imageUrl
) {}

