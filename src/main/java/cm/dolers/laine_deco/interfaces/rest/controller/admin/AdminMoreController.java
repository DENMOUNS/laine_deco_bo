package cm.dolers.laine_deco.interfaces.rest.controller.admin;

/**
 * @deprecated Ce fichier a été séparé en contrôleurs individuels pour respecter le principe SRP.
 * 
 * Classes maintenant dans les fichiers séparés:
 * - AdminKnittingPatternControllerSep.java (Pattern management)
 * - AdminPromoEventControllerSep.java (Promo events)
 * - AdminRmaControllerSep.java (RMA management)
 * - AdminSiteConfigControllerSep.java (Site configuration)
 * 
 * Ce fichier peut être supprimé.
 */
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminKnittingPatternController {
    private final KnittingPatternService patternService;

    @PostMapping
    public ResponseEntity<KnittingPatternResponse> createPattern(@Valid @RequestBody CreateKnittingPatternRequest request) {
        log.info("POST /api/admin/knitting-patterns - Creating: {}", request.name());
        var response = patternService.createPattern(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KnittingPatternResponse> getPattern(@PathVariable Long id) {
        log.info("GET /api/admin/knitting-patterns/{}", id);
        var response = patternService.getPatternById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<KnittingPatternResponse>> getAllPatterns(Pageable pageable) {
        log.info("GET /api/admin/knitting-patterns");
        var response = patternService.getAllPatterns(pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<KnittingPatternResponse> updatePattern(@PathVariable Long id,
            @Valid @RequestBody CreateKnittingPatternRequest request) {
        log.info("PUT /api/admin/knitting-patterns/{}", id);
        var response = patternService.updatePattern(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePattern(@PathVariable Long id) {
        log.info("DELETE /api/admin/knitting-patterns/{}", id);
        patternService.deletePattern(id);
        return ResponseEntity.noContent().build();
    }
}

/**
 * Admin Controller pour Promo Events
 */
@RestController
@RequestMapping("/api/admin/promo-events")
@RequiredArgsConstructor
class AdminPromoEventController {
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

/**
 * Admin Controller pour RMA
 */
@RestController
@RequestMapping("/api/admin/rmas")
@RequiredArgsConstructor
class AdminRmaController {
    private final RmaService rmaService;

    @GetMapping("/{id}")
    public ResponseEntity<RmaResponse> getRma(@PathVariable Long id) {
        log.info("GET /api/admin/rmas/{}", id);
        var response = rmaService.getRmaById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/number/{rmaNumber}")
    public ResponseEntity<RmaResponse> getRmaByNumber(@PathVariable String rmaNumber) {
        log.info("GET /api/admin/rmas/number/{}", rmaNumber);
        var response = rmaService.getRmaByNumber(rmaNumber);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<RmaResponse>> getAllRmas(Pageable pageable) {
        log.info("GET /api/admin/rmas");
        var response = rmaService.getAllRmas(pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Void> approveRma(@PathVariable Long id) {
        log.info("POST /api/admin/rmas/{}/approve", id);
        rmaService.approveRma(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<Void> rejectRma(@PathVariable Long id) {
        log.info("POST /api/admin/rmas/{}/reject", id);
        rmaService.rejectRma(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/resolve")
    public ResponseEntity<Void> resolveRma(@PathVariable Long id) {
        log.info("POST /api/admin/rmas/{}/resolve", id);
        rmaService.resolveRma(id);
        return ResponseEntity.ok().build();
    }
}

/**
 * Admin Controller pour Site Configuration
 */
@RestController
@RequestMapping("/api/admin/site-config")
@RequiredArgsConstructor
class AdminSiteConfigController {
    private final SiteConfigService siteConfigService;

    @GetMapping("/{key}")
    public ResponseEntity<SiteConfigResponse> getConfig(@PathVariable String key) {
        log.info("GET /api/admin/site-config/{}", key);
        var response = siteConfigService.getConfigByKey(key);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<SiteConfigResponse>> getAllConfigs(Pageable pageable) {
        log.info("GET /api/admin/site-config");
        var response = siteConfigService.getAllConfigs(pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<SiteConfigResponse> updateConfig(@Valid @RequestBody UpdateSiteConfigRequest request) {
        log.info("PUT /api/admin/site-config - Key: {}", request.key());
        var response = siteConfigService.updateConfig(request);
        return ResponseEntity.ok(response);
    }
}
