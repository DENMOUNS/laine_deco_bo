package cm.dolers.laine_deco.application.dto;

/**
 * Critères de recherche de produits avec filtres
 */
public record ProductFilterRequest(
    String keyword,
    Long categoryId,
    String brand,
    Double minPrice,
    Double maxPrice,
    Integer minRating,
    Boolean inStockOnly,
    String sortBy,
    Integer page,
    Integer pageSize
) {}

