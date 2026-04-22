package cm.dolers.laine_deco.interfaces.rest.controller.client;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.RmaService;
import cm.dolers.laine_deco.infrastructure.security.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client/rmas")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CLIENT')")
public class ClientRmaController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ClientRmaController.class);
    private final RmaService rmaService;

    @PostMapping
    public ResponseEntity<RmaResponse> createRma(@Valid @RequestBody CreateRmaRequest request) {
        log.info("POST /api/client/rmas - Order: {}", request.orderId());
        return ResponseEntity.status(HttpStatus.CREATED).body(rmaService.createRma(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RmaResponse> getRma(@PathVariable Long id) {
        log.info("GET /api/client/rmas/{}", id);
        return ResponseEntity.ok(rmaService.getRmaById(id));
    }

    @GetMapping("/me")
    public ResponseEntity<Page<RmaResponse>> getMyRmas(Pageable pageable) {
        Long userId = SecurityUtils.getCurrentUserId();
        log.info("GET /api/client/rmas/me - User: {}", userId);
        return ResponseEntity.ok(rmaService.getUserRmas(userId, pageable));
    }

    @GetMapping("/number/{rmaNumber}")
    public ResponseEntity<RmaResponse> getRmaByNumber(@PathVariable String rmaNumber) {
        log.info("GET /api/client/rmas/number/{}", rmaNumber);
        return ResponseEntity.ok(rmaService.getRmaByNumber(rmaNumber));
    }
}
