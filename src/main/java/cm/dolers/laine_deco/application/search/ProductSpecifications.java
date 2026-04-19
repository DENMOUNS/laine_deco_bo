package cm.dolers.laine_deco.application.search;

import cm.dolers.laine_deco.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Specifications JPA pour construire des requêtes dynamiques sur les produits
 * Permet une recherche performante avec wildcards et filtres avancés
 */
public class ProductSpecifications {

    /**
     * Crée une specification complète basée sur les critères
     */
    public static Specification<ProductEntity> byCriteria(
            String keyword,
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Integer minRating,
            Boolean inStock) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Recherche texte (nom, description, SKU)
            if (keyword != null && !keyword.isBlank()) {
                String searchPattern = "%" + keyword.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("name")), searchPattern),
                        cb.like(cb.lower(root.get("description")), searchPattern),
                        cb.like(root.get("sku"), searchPattern)));
            }

            // Filtre par catégorie
            if (categoryId != null) {
                predicates.add(cb.equal(root.get("categoryId"), categoryId));
            }

            // Filtre par prix minimum
            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("salePrice"), minPrice));
            }

            // Filtre par prix maximum
            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("salePrice"), maxPrice));
            }

            // Filtre par rating
            if (minRating != null && minRating > 0) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("rating"), (double) minRating));
            }

            // Filtre stock uniquement
            if (inStock != null && inStock) {
                predicates.add(cb.greaterThan(root.get("stockQuantity"), 0));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Specification pour recherche texte uniquement (pour auto-complétion)
     */
    public static Specification<ProductEntity> byTextSearch(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) {
                return cb.conjunction();
            }
            String searchPattern = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("name")), searchPattern),
                    cb.like(cb.lower(root.get("description")), searchPattern),
                    cb.like(root.get("sku"), searchPattern));
        };
    }

    /**
     * Specification pour produits en stock
     */
    public static Specification<ProductEntity> inStock() {
        return (root, query, cb) -> cb.greaterThan(root.get("stockQuantity"), 0);
    }

    /**
     * Specification pour produits par catégorie
     */
    public static Specification<ProductEntity> byCategory(Long categoryId) {
        return (root, query, cb) -> {
            if (categoryId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("categoryId"), categoryId);
        };
    }

    /**
     * Specification pour produits dans une plage de prix
     */
    public static Specification<ProductEntity> byPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("salePrice"), minPrice));
            }

            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("salePrice"), maxPrice));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Specification pour produits avec rating minimum
     */
    public static Specification<ProductEntity> byMinimumRating(Integer minRating) {
        return (root, query, cb) -> {
            if (minRating == null || minRating <= 0) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("rating"), (double) minRating);
        };
    }
}
