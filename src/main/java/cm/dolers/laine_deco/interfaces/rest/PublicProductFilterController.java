package cm.dolers.laine_deco.interfaces.rest;

import cm.dolers.laine_deco.application.dto.ProductFilterRequest;
import cm.dolers.laine_deco.application.dto.ProductResponse;
import cm.dolers.laine_deco.application.usecase.impl.ProductFilterServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint public pour filtrer les produits par catégories, marques, prix, etc.
 */
@RestController
@RequestMapping("/api/public/products")
@RequiredArgsConstructor
@Slf4j
public class PublicProductFilterController {
    private final ProductFilterServiceImpl productFilterService;

    /**
     * GET /api/public/products/filter
     * Filtre les produits avec de multiples critères
     *
     * @param keyword Mot clé de recherche
     * @param categoryId ID de la catégorie
     * @param brand Marque du produit
     * @param minPrice Prix minimum
     * @param maxPrice Prix maximum
     * @param minRating Note minimale (1-5)
     * @param inStockOnly Si true, seulement les produits en stock
     * @param sortBy Critère de tri (PRICE_ASC, PRICE_DESC, RATING, NEWEST, POPULARITY)
     * @param page Numéro de page (0-indexed)
     * @param pageSize Taille de page (5, 10, 25, 50, 100, 250)
     */
    @GetMapping("/filter")
    public ResponseEntity<Page<ProductResponse>> filterProducts(
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) Long categoryId,
        @RequestParam(required = false) String brand,
        @RequestParam(required = false) Double minPrice,
        @RequestParam(required = false) Double maxPrice,
        @RequestParam(required = false) Integer minRating,
        @RequestParam(required = false, defaultValue = "false") Boolean inStockOnly,
        @RequestParam(required = false, defaultValue = "NAME") String sortBy,
        @RequestParam(required = false, defaultValue = "0") Integer page,
        @RequestParam(required = false, defaultValue = "10") Integer pageSize
    ) {
        log.info("GET /api/public/products/filter - Filtering products: keyword={}, categoryId={}, brand={}, page={}, pageSize={}",
            keyword, categoryId, brand, page, pageSize);

        ProductFilterRequest filterRequest = new ProductFilterRequest(
            keyword, categoryId, brand, minPrice, maxPrice, minRating,
            inStockOnly, sortBy, page, pageSize
        );

        Page<ProductResponse> results = productFilterService.filterProducts(filterRequest);
        return ResponseEntity.ok(results);
    }

    /**
     * GET /api/public/products/by-category/:categoryId
     * Récupère les produits d'une catégorie spécifique
     */
    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<Page<ProductResponse>> getProductsByCategory(
        @PathVariable Long categoryId,
        @RequestParam(required = false, defaultValue = "0") Integer page,
        @RequestParam(required = false, defaultValue = "10") Integer pageSize
    ) {
        log.info("GET /api/public/products/by-category/{} - page={}, pageSize={}", categoryId, page, pageSize);

        ProductFilterRequest filterRequest = new ProductFilterRequest(
            null, categoryId, null, null, null, null, null, null, page, pageSize
        );

        Page<ProductResponse> results = productFilterService.filterProducts(filterRequest);
        return ResponseEntity.ok(results);
    }

    /**
     * GET /api/public/products/by-brand/:brand
     * Récupère les produits d'une marque spécifique
     */
    @GetMapping("/by-brand/{brand}")
    public ResponseEntity<Page<ProductResponse>> getProductsByBrand(
        @PathVariable String brand,
        @RequestParam(required = false, defaultValue = "0") Integer page,
        @RequestParam(required = false, defaultValue = "10") Integer pageSize
    ) {
        log.info("GET /api/public/products/by-brand/{} - page={}, pageSize={}", brand, page, pageSize);

        ProductFilterRequest filterRequest = new ProductFilterRequest(
            null, null, brand, null, null, null, null, null, page, pageSize
        );

        Page<ProductResponse> results = productFilterService.filterProducts(filterRequest);
        return ResponseEntity.ok(results);
    }
}
