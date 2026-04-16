package cm.dolers.laine_deco.interfaces.rest.controller.admin;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.RmaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Admin Controller pour RMA (Return Merchandise Authorization)
 * Responsabilité: Gérer les retours de produits et générer les autorisations RMA
 */
@RestController
@RequestMapping("/api/admin/rmas")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminRmaController {
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
