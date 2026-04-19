package cm.dolers.laine_deco.interfaces.rest.controller.admin;


import cm.dolers.laine_deco.application.dto.ProductSearchCriteria;
import cm.dolers.laine_deco.application.dto.ProductResponse;
import cm.dolers.laine_deco.application.usecase.ProductSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * Admin Controller pour la recherche et analyse des produits
 */
@RestController
@RequestMapping("/api/admin/products/advanced-search")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductSearchController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AdminProductSearchController.class);
    private final ProductSearchService productSearchService;

    /**
     * Recherche avancée avec critères (Admin)
     */
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> searchAdvanced(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer minRating,
            @RequestParam(required = false) Boolean inStock,
            @RequestParam(defaultValue = "PRICE_ASC") String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int pageSize) {

        log.info("ADMIN GET /api/admin/products/advanced-search - keyword: {}, category: {}", keyword, categoryId);

        var criteria = new ProductSearchCriteria(
            keyword, categoryId, minPrice, maxPrice, minRating, inStock, sortBy, page, pageSize
        );

        return ResponseEntity.ok(productSearchService.searchByCriteria(criteria));
    }

    /**
     * Produits populaires (Admin)
     */
    @GetMapping("/analytics/popular")
    public ResponseEntity<Page<ProductResponse>> getPopularForAnalytics(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int pageSize) {

        log.info("ADMIN GET /api/admin/products/advanced-search/analytics/popular");

        return ResponseEntity.ok(productSearchService.getPopularProducts(page, pageSize));
    }

    /**
     * Produits avec faibles ventes (potentially problematic inventory)
     */
    @GetMapping("/analytics/low-performers")
    public ResponseEntity<Page<ProductResponse>> getLowPerformers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int pageSize) {

        log.info("ADMIN GET /api/admin/products/advanced-search/analytics/low-performers");

        // Recherche produits non populaires (0-100 viewCount)
        var criteria = new ProductSearchCriteria(
            null, null, null, null, null, true, "POPULARITY", page, pageSize
        );

        return ResponseEntity.ok(productSearchService.searchByCriteria(criteria));
    }

    /**
     * Produits les mieux notés (Admin)
     */
    @GetMapping("/analytics/top-rated")
    public ResponseEntity<Page<ProductResponse>> getTopRatedForAnalytics(
            @RequestParam(required = false) Integer minRating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int pageSize) {

        log.info("ADMIN GET /api/admin/products/advanced-search/analytics/top-rated");

        return ResponseEntity.ok(productSearchService.getTopRatedProducts(minRating, page, pageSize));
    }
}


