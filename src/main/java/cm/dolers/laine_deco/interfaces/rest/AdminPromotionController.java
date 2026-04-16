package cm.dolers.laine_deco.interfaces.rest;

import cm.dolers.laine_deco.application.dto.CreatePromotionRequest;
import cm.dolers.laine_deco.application.dto.PromotionResponse;
import cm.dolers.laine_deco.application.usecase.PromotionService;
import cm.dolers.laine_deco.infrastructure.i18n.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoint ADMIN pour gérer les promotions
 */
@RestController
@RequestMapping("/api/admin/promotions")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminPromotionController {
    private final PromotionService promotionService;
    private final MessageService messageService;

    /**
     * GET /api/admin/promotions
     * Récupère toutes les promotions actives
     */
    @GetMapping
    public ResponseEntity<List<PromotionResponse>> getAllPromotions() {
        log.info("GET /api/admin/promotions - Fetching all active promotions");
        List<PromotionResponse> promotions = promotionService.getActivePromotions();
        return ResponseEntity.ok(promotions);
    }

    /**
     * GET /api/admin/promotions/flash-sales
     * Récupère les ventes flash actuelles
     */
    @GetMapping("/flash-sales")
    public ResponseEntity<List<PromotionResponse>> getFlashSales() {
        log.info("GET /api/admin/promotions/flash-sales");
        List<PromotionResponse> flashSales = promotionService.getActiveFlashSales();
        return ResponseEntity.ok(flashSales);
    }

    /**
     * POST /api/admin/promotions
     * Crée une nouvelle promotion
     */
    @PostMapping
    public ResponseEntity<PromotionResponse> createPromotion(@RequestBody CreatePromotionRequest request) {
        log.info("POST /api/admin/promotions - Creating new promotion: {}", request.name());

        try {
            PromotionResponse response = promotionService.createPromotion(
                request.name(),
                request.description(),
                request.type(),
                request.startDate(),
                request.endDate(),
                request.discountPercentage(),
                request.discountAmount(),
                request.categoryId(),
                request.brand(),
                request.productId()
            );
            
            log.info("Promotion created successfully: {}", request.name());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error creating promotion: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * PUT /api/admin/promotions/:id
     * Met à jour une promotion
     */
    @PutMapping("/{id}")
    public ResponseEntity<PromotionResponse> updatePromotion(
        @PathVariable Long id,
        @RequestBody CreatePromotionRequest request
    ) {
        log.info("PUT /api/admin/promotions/{} - Updating promotion", id);

        try {
            PromotionResponse response = promotionService.updatePromotion(
                id,
                request.name(),
                request.description(),
                request.startDate(),
                request.endDate(),
                request.discountPercentage(),
                request.discountAmount()
            );
            
            log.info("Promotion updated successfully: {}", id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Promotion not found: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error updating promotion: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * DELETE /api/admin/promotions/:id
     * Désactive une promotion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromotion(@PathVariable Long id) {
        log.info("DELETE /api/admin/promotions/{}", id);

        try {
            promotionService.deactivatePromotion(id);
            log.info("Promotion deactivated: {}", id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Promotion not found: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * POST /api/admin/promotions/cleanup-expired
     * Désactive toutes les promotions expirées
     */
    @PostMapping("/cleanup-expired")
    public ResponseEntity<String> cleanupExpiredPromotions() {
        log.info("POST /api/admin/promotions/cleanup-expired");

        try {
            promotionService.deactivateExpiredPromotions();
            return ResponseEntity.ok(messageService.getMessage("success.operation.completed"));
        } catch (Exception e) {
            log.error("Error cleaning up expired promotions: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
