package cm.dolers.laine_deco.interfaces.rest;

import cm.dolers.laine_deco.application.dto.PromotionResponse;
import cm.dolers.laine_deco.application.usecase.PromotionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoint PUBLIC pour consulter les promotions actives
 */
@RestController
@RequestMapping("/api/public/promotions")
@RequiredArgsConstructor
@Slf4j
public class PublicPromotionController {
    private final PromotionService promotionService;

    /**
     * GET /api/public/promotions
     * Récupère toutes les promotions actuellement actives
     */
    @GetMapping
    public ResponseEntity<List<PromotionResponse>> getActivePromotions() {
        log.info("GET /api/public/promotions - Fetching active promotions");
        List<PromotionResponse> promotions = promotionService.getActivePromotions();
        return ResponseEntity.ok(promotions);
    }

    /**
     * GET /api/public/promotions/flash-sales
     * Récupère les ventes flash actuelles avec timers
     */
    @GetMapping("/flash-sales")
    public ResponseEntity<List<PromotionResponse>> getFlashSales() {
        log.info("GET /api/public/promotions/flash-sales");
        List<PromotionResponse> flashSales = promotionService.getActiveFlashSales();
        return ResponseEntity.ok(flashSales);
    }

    /**
     * GET /api/public/promotions/category/:categoryId
     * Récupère les promotions actives pour une catégorie
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<PromotionResponse>> getPromotionsForCategory(@PathVariable Long categoryId) {
        log.info("GET /api/public/promotions/category/{}", categoryId);
        List<PromotionResponse> promotions = promotionService.getPromotionForCategory(categoryId);
        return ResponseEntity.ok(promotions);
    }

    /**
     * GET /api/public/promotions/brand/:brand
     * Récupère les promotions actives pour une marque
     */
    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<PromotionResponse>> getPromotionsForBrand(@PathVariable String brand) {
        log.info("GET /api/public/promotions/brand/{}", brand);
        List<PromotionResponse> promotions = promotionService.getPromotionForBrand(brand);
        return ResponseEntity.ok(promotions);
    }

    /**
     * GET /api/public/promotions/product/:productId
     * Récupère les promotions actives pour un produit (ex: vente flash)
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<PromotionResponse>> getPromotionsForProduct(@PathVariable Long productId) {
        log.info("GET /api/public/promotions/product/{}", productId);
        List<PromotionResponse> promotions = promotionService.getPromotionForProduct(productId);
        return ResponseEntity.ok(promotions);
    }
}
