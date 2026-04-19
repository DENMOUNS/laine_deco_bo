package cm.dolers.laine_deco.interfaces.rest.controller.admin;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.PromoEventService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/promo-events")
@RequiredArgsConstructor

@PreAuthorize("hasRole('ADMIN')")
public class AdminPromoEventController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AdminPromoEventController.class);
    private final PromoEventService promoEventService;

    @PostMapping
    public ResponseEntity<PromoEventResponse> createPromoEvent(@Valid @RequestBody CreatePromoEventRequest request) {
        log.info("POST /api/admin/promo-events - Creating: {}", request.name());
        var response = promoEventService.createPromoEvent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromoEventResponse> getPromoEvent(@PathVariable Long id) {
        log.info("GET /api/admin/promo-events/{}", id);
        var response = promoEventService.getPromoEventById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<PromoEventResponse>> getAllPromoEvents(Pageable pageable) {
        log.info("GET /api/admin/promo-events");
        var response = promoEventService.getAllPromoEvents(pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PromoEventResponse> updatePromoEvent(@PathVariable Long id,
            @Valid @RequestBody CreatePromoEventRequest request) {
        log.info("PUT /api/admin/promo-events/{}", id);
        var response = promoEventService.updatePromoEvent(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromoEvent(@PathVariable Long id) {
        log.info("DELETE /api/admin/promo-events/{}", id);
        promoEventService.deletePromoEvent(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<Void> activatePromoEvent(@PathVariable Long id) {
        log.info("POST /api/admin/promo-events/{}/activate", id);
        promoEventService.activatePromoEvent(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivatePromoEvent(@PathVariable Long id) {
        log.info("POST /api/admin/promo-events/{}/deactivate", id);
        promoEventService.deactivatePromoEvent(id);
        return ResponseEntity.ok().build();
    }
}
