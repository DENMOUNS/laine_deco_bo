package cm.dolers.laine_deco.application.dto;


import java.math.BigDecimal;

/**
 * Critères de recherche pour les produits
 * Utilisé pour filtrer les résultats de manière performante
 */
public record ProductSearchCriteria(
    String keyword,                    // Recherche sur nom, description, SKU
    Long categoryId,                   // Filtre par catégorie
    BigDecimal minPrice,               // Prix minimum
    BigDecimal maxPrice,               // Prix maximum
    Integer minRating,                 // Évaluation minimale (1-5)
    Boolean inStock,                   // Disponible en stock (uniquement)
    String sortBy,                     // PRICE_ASC, PRICE_DESC, RATING, NEWEST, POPULARITY
    Integer pageNumber,                // Numéro de page (0-based)
    Integer pageSize                   // Taille de page (max 100)
) {}

