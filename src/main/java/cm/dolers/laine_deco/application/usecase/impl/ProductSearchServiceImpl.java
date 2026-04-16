package cm.dolers.laine_deco.application.usecase.impl;

import cm.dolers.laine_deco.application.dto.ProductResponse;
import cm.dolers.laine_deco.application.dto.ProductSearchCriteria;
import cm.dolers.laine_deco.application.mapper.ProductMapper;
import cm.dolers.laine_deco.application.search.ProductSpecifications;
import cm.dolers.laine_deco.application.usecase.ProductSearchService;
import cm.dolers.laine_deco.infrastructure.persistence.entity.ProductEntity;
import cm.dolers.laine_deco.infrastructure.persistence.repository.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductSearchServiceImpl implements ProductSearchService {
    private final ProductJpaRepository productRepository;
    private final ProductMapper productMapper;

    private static final int MAX_PAGE_SIZE = 100;

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> searchByCriteria(ProductSearchCriteria criteria) {
        log.info("Searching products by criteria - keyword: {}, category: {}, price: {} - {}", 
            criteria.keyword(), criteria.categoryId(), criteria.minPrice(), criteria.maxPrice());

        // Valider la pagination
        int pageSize = Math.min(criteria.pageSize() != null ? criteria.pageSize() : 20, MAX_PAGE_SIZE);
        int pageNumber = criteria.pageNumber() != null ? criteria.pageNumber() : 0;

        // Créer le sort selon le critère
        Sort sort = buildSort(criteria.sortBy());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        // Construire la specification avec tous les critères
        Specification<ProductEntity> spec = ProductSpecifications.byCriteria(
            criteria.keyword(),
            criteria.categoryId(),
            criteria.minPrice(),
            criteria.maxPrice(),
            criteria.minRating(),
            criteria.inStock()
        );

        // Exécuter la recherche
        Page<ProductEntity> results = productRepository.findAll(spec, pageable);
        log.info("Found {} products", results.getTotalElements());

        return results.map(productMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> searchByKeyword(String keyword, int page, int pageSize) {
        log.info("Searching by keyword: {}", keyword);

        pageSize = Math.min(pageSize, MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("viewsCount").descending().and(Sort.by("name").ascending()));

        Page<ProductEntity> results = productRepository.searchByKeyword(keyword, pageable);
        log.info("Found {} products for keyword: {}", results.getTotalElements(), keyword);

        return results.map(productMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getPopularProducts(int page, int pageSize) {
        log.info("Fetching popular products");

        pageSize = Math.min(pageSize, MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<ProductEntity> results = productRepository.findPopularProducts(pageable);
        return results.map(productMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getTopRatedProducts(Integer minRating, int page, int pageSize) {
        log.info("Fetching top rated products - minRating: {}", minRating);

        pageSize = Math.min(pageSize, MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(page, pageSize);

        int rating = minRating != null ? minRating : 4; // Par défaut 4+
        Page<ProductEntity> results = productRepository.findTopRatedProducts(rating, pageable);

        return results.map(productMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getNewestProducts(int page, int pageSize) {
        log.info("Fetching newest products");

        pageSize = Math.min(pageSize, MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<ProductEntity> results = productRepository.findNewestProducts(pageable);
        return results.map(productMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getSearchSuggestions(String prefix) {
        log.info("Generating search suggestions for prefix: {}", prefix);

        if (prefix == null || prefix.isBlank() || prefix.length() < 2) {
            return List.of();
        }

        return productRepository.findProductNameSuggestions(prefix);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsByCategory(Long categoryId, int page, int pageSize) {
        log.info("Fetching products by category: {}", categoryId);

        pageSize = Math.min(pageSize, MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("name").ascending());

        Page<ProductEntity> results = productRepository.findByCategoryIdAndStockQuantityGreaterThan(
            categoryId, 0, pageable
        );

        return results.map(productMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getInStockProducts(int page, int pageSize) {
        log.info("Fetching in-stock products");

        pageSize = Math.min(pageSize, MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(page, pageSize);

        Specification<ProductEntity> spec = ProductSpecifications.inStock();
        Page<ProductEntity> results = productRepository.findAll(spec, pageable);

        return results.map(productMapper::toResponse);
    }

    /**
     * Construit le tri selon le critère demandé
     */
    private Sort buildSort(String sortBy) {
        if (sortBy == null) {
            return Sort.by("name").ascending();
        }

        return switch (sortBy) {
            case "PRICE_ASC" -> Sort.by("salePrice").ascending();
            case "PRICE_DESC" -> Sort.by("salePrice").descending();
            case "RATING" -> Sort.by("rating").descending().and(Sort.by("salesCount").descending());
            case "NEWEST" -> Sort.by("createdAt").descending();
            case "POPULARITY" -> Sort.by("viewsCount").descending().and(Sort.by("salesCount").descending());
            default -> Sort.by("name").ascending();
        };
    }
}
