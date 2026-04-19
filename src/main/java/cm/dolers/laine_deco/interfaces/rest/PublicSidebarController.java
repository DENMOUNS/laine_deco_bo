package cm.dolers.laine_deco.interfaces.rest;

import cm.dolers.laine_deco.application.dto.CategoryWithCountResponse;
import cm.dolers.laine_deco.application.dto.BrandWithCountResponse;
import cm.dolers.laine_deco.application.dto.SidebarFiltersResponse;
import cm.dolers.laine_deco.application.usecase.SidebarFiltersService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Endpoint public pour les filtres du sidebar (catégories, marques)
 */
@RestController
@RequestMapping("/api/public/sidebar")
@RequiredArgsConstructor

public class PublicSidebarController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PublicSidebarController.class);
    private final SidebarFiltersService sidebarFiltersService;

    /**
     * GET /api/public/sidebar/filters
     * Récupère tous les filtres du sidebar (catégories, marques, totaux)
     */
    @GetMapping("/filters")
    public ResponseEntity<SidebarFiltersResponse> getSidebarFilters() {
        log.info("GET /api/public/sidebar/filters - Fetching sidebar filters");
        SidebarFiltersResponse filters = sidebarFiltersService.getSidebarFilters();
        return ResponseEntity.ok(filters);
    }

    /**
     * GET /api/public/sidebar/categories
     * Récupère toutes les catégories avec le count de produits
     */
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryWithCountResponse>> getCategories() {
        log.info("GET /api/public/sidebar/categories - Fetching categories with count");
        List<CategoryWithCountResponse> categories = sidebarFiltersService.getCategoriesWithCount();
        return ResponseEntity.ok(categories);
    }

    /**
     * GET /api/public/sidebar/brands
     * Récupère toutes les marques avec le count de produits
     */
    @GetMapping("/brands")
    public ResponseEntity<List<BrandWithCountResponse>> getBrands() {
        log.info("GET /api/public/sidebar/brands - Fetching brands with count");
        List<BrandWithCountResponse> brands = sidebarFiltersService.getBrandsWithCount();
        return ResponseEntity.ok(brands);
    }

    /**
     * GET /api/public/sidebar/stats
     * Récupère les statistiques (total produits, produits en stock)
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStats() {
        log.info("GET /api/public/sidebar/stats - Fetching statistics");
        return ResponseEntity.ok(Map.of(
            "totalProducts", sidebarFiltersService.getTotalProductCount(),
            "productsInStock", sidebarFiltersService.getInStockProductCount()
        ));
    }
}


