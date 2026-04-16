package cm.dolers.laine_deco.interfaces.rest.controller.public_api;

import cm.dolers.laine_deco.application.dto.PromoEventResponse;
import cm.dolers.laine_deco.application.usecase.PromoEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Public Controller pour Promo Events
 * Responsabilité: Afficher les événements promotionnels actifs
 */
@RestController
@RequestMapping("/api/public/promo-events")
@RequiredArgsConstructor
@Slf4j
public class PublicPromoEventController {
    private final PromoEventService promoEventService;

    @GetMapping("/{id}")
    public ResponseEntity<PromoEventResponse> getPromoEvent(@PathVariable Long id) {
        log.info("GET /api/public/promo-events/{}", id);
        var response = promoEventService.getPromoEventById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<PromoEventResponse>> getActivePromoEvents(Pageable pageable) {
        log.info("GET /api/public/promo-events");
        var response = promoEventService.getActivePromoEvents(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/featured")
    public ResponseEntity<Page<PromoEventResponse>> getFeaturedPromoEvents(Pageable pageable) {
        log.info("GET /api/public/promo-events/featured");
        var response = promoEventService.getFeaturedPromoEvents(pageable);
        return ResponseEntity.ok(response);
    }
}
