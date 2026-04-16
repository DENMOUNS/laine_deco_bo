package cm.dolers.laine_deco.interfaces.rest.controller.public_api;

import cm.dolers.laine_deco.application.dto.ProductResponse;
import cm.dolers.laine_deco.application.dto.ProductSearchCriteria;
import cm.dolers.laine_deco.application.usecase.ProductSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Public Controller pour la recherche de produits rapide et avancée
 * Endpoints: /api/public/products/search
 */
@RestController
@RequestMapping("/api/public/products/search")
@RequiredArgsConstructor
@Slf4j
public class PublicProductSearchController {
    private final ProductSearchService productSearchService;

    /**
     * Recherche avancée avec critères multiples
     * Query parameters: keyword, categoryId, minPrice, maxPrice, minRating, inStock, sortBy, page, pageSize
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
            @RequestParam(defaultValue = "20") int pageSize) {

        log.info("GET /api/public/products/search - keyword: {}, category: {}, sortBy: {}", 
            keyword, categoryId, sortBy);

        var criteria = new ProductSearchCriteria(
            keyword, categoryId, minPrice, maxPrice, minRating, inStock, sortBy, page, pageSize
        );

        var results = productSearchService.searchByCriteria(criteria);
        return ResponseEntity.ok(results);
    }

    /**
     * Recherche texte simple (rapide)
     */
    @GetMapping("/keyword")
    public ResponseEntity<Page<ProductResponse>> searchByKeyword(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize) {

        log.info("GET /api/public/products/search/keyword - keyword: {}", keyword);

        var results = productSearchService.searchByKeyword(keyword, page, pageSize);
        return ResponseEntity.ok(results);
    }

    /**
     * Produits populaires
     */
    @GetMapping("/popular")
    public ResponseEntity<Page<ProductResponse>> getPopular(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize) {

        log.info("GET /api/public/products/search/popular");

        var results = productSearchService.getPopularProducts(page, pageSize);
        return ResponseEntity.ok(results);
    }

    /**
     * Produits les mieux notés
     */
    @GetMapping("/top-rated")
    public ResponseEntity<Page<ProductResponse>> getTopRated(
            @RequestParam(required = false) Integer minRating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize) {

        log.info("GET /api/public/products/search/top-rated - minRating: {}", minRating);

        var results = productSearchService.getTopRatedProducts(minRating, page, pageSize);
        return ResponseEntity.ok(results);
    }

    /**
     * Nouveaux produits
     */
    @GetMapping("/newest")
    public ResponseEntity<Page<ProductResponse>> getNewest(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize) {

        log.info("GET /api/public/products/search/newest");

        var results = productSearchService.getNewestProducts(page, pageSize);
        return ResponseEntity.ok(results);
    }

    /**
     * Suggestions d'auto-complétion
     */
    @GetMapping("/suggestions")
    public ResponseEntity<List<String>> getSuggestions(
            @RequestParam String prefix) {

        log.info("GET /api/public/products/search/suggestions - prefix: {}", prefix);

        var suggestions = productSearchService.getSearchSuggestions(prefix);
        return ResponseEntity.ok(suggestions);
    }

    /**
     * Produits par catégorie
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ProductResponse>> getByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize) {

        log.info("GET /api/public/products/search/category/{} - page: {}, size: {}", categoryId, page, pageSize);

        var results = productSearchService.getProductsByCategory(categoryId, page, pageSize);
        return ResponseEntity.ok(results);
    }

    /**
     * Produits en stock
     */
    @GetMapping("/in-stock")
    public ResponseEntity<Page<ProductResponse>> getInStock(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize) {

        log.info("GET /api/public/products/search/in-stock");

        var results = productSearchService.getInStockProducts(page, pageSize);
        return ResponseEntity.ok(results);
    }
}
