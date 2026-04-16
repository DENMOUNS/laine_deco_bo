package cm.dolers.laine_deco.application.usecase.impl;

import cm.dolers.laine_deco.application.dto.ProductFilterRequest;
import cm.dolers.laine_deco.application.dto.ProductResponse;
import cm.dolers.laine_deco.application.mapper.ProductMapper;
import cm.dolers.laine_deco.infrastructure.persistence.entity.ProductEntity;
import cm.dolers.laine_deco.infrastructure.persistence.repository.ProductJpaRepository;
import cm.dolers.laine_deco.infrastructure.config.PaginationConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProductFilterServiceImpl {
    private final ProductJpaRepository productRepository;
    private final ProductMapper productMapper;

    /**
     * Recherche les produits selon les critères de filtrage
     */
    public Page<ProductResponse> filterProducts(ProductFilterRequest filterRequest) {
        log.info("Filtering products with criteria: keyword={}, categoryId={}, brand={}",
            filterRequest.keyword(), filterRequest.categoryId(), filterRequest.brand());

        // Normaliser la pagination
        int page = filterRequest.page() != null ? filterRequest.page() : 0;
        int pageSize = PaginationConstants.normalizePageSize(
            filterRequest.pageSize() != null ? filterRequest.pageSize() : PaginationConstants.DEFAULT_PAGE_SIZE
        );

        // Construire la spécification pour les filtres
        Specification<ProductEntity> spec = buildSpecification(filterRequest);

        // Déterminer le tri
        Sort sort = buildSort(filterRequest.sortBy());

        // Créer la pagination
        Pageable pageable = PageRequest.of(page, pageSize, sort);

        // Rechercher les produits
        Page<ProductEntity> products = productRepository.findAll(spec, pageable);

        return products.map(productMapper::toResponse);
    }

    /**
     * Construit la spécification pour les filtres
     */
    private Specification<ProductEntity> buildSpecification(ProductFilterRequest filterRequest) {
        return Specification.where(
            hasKeyword(filterRequest.keyword())
                .and(hasCategory(filterRequest.categoryId()))
                .and(hasBrand(filterRequest.brand()))
                .and(priceBetween(filterRequest.minPrice(), filterRequest.maxPrice()))
                .and(hasMinRating(filterRequest.minRating()))
                .and(isInStock(filterRequest.inStockOnly()))
        );
    }

    private Specification<ProductEntity> hasKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) {
                return cb.conjunction();
            }
            String pattern = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                cb.like(cb.lower(root.get("name")), pattern),
                cb.like(cb.lower(root.get("description")), pattern)
            );
        };
    }

    private Specification<ProductEntity> hasCategory(Long categoryId) {
        return (root, query, cb) -> {
            if (categoryId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("category").get("id"), categoryId);
        };
    }

    private Specification<ProductEntity> hasBrand(String brand) {
        return (root, query, cb) -> {
            if (brand == null || brand.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("brand"), brand);
        };
    }

    private Specification<ProductEntity> priceBetween(Double minPrice, Double maxPrice) {
        return (root, query, cb) -> {
            if (minPrice == null && maxPrice == null) {
                return cb.conjunction();
            }
            if (minPrice != null && maxPrice != null) {
                return cb.between(root.get("salePrice"), minPrice, maxPrice);
            }
            if (minPrice != null) {
                return cb.greaterThanOrEqualTo(root.get("salePrice"), minPrice);
            }
            return cb.lessThanOrEqualTo(root.get("salePrice"), maxPrice);
        };
    }

    private Specification<ProductEntity> hasMinRating(Integer minRating) {
        return (root, query, cb) -> {
            if (minRating == null || minRating <= 0) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("rating"), minRating);
        };
    }

    private Specification<ProductEntity> isInStock(Boolean inStockOnly) {
        return (root, query, cb) -> {
            if (inStockOnly == null || !inStockOnly) {
                return cb.conjunction();
            }
            return cb.greaterThan(root.get("stockQuantity"), 0);
        };
    }

    /**
     * Construit l'ordre de tri
     */
    private Sort buildSort(String sortBy) {
        if (sortBy == null || sortBy.isBlank()) {
            return Sort.by("name").ascending();
        }

        return switch (sortBy.toUpperCase()) {
            case "PRICE_ASC" -> Sort.by("salePrice").ascending();
            case "PRICE_DESC" -> Sort.by("salePrice").descending();
            case "RATING" -> Sort.by("rating").descending();
            case "NEWEST" -> Sort.by("createdAt").descending();
            case "POPULARITY" -> Sort.by("viewsCount").descending();
            default -> Sort.by("name").ascending();
        };
    }
}
