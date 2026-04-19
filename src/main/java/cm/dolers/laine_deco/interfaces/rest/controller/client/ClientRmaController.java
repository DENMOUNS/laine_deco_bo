package cm.dolers.laine_deco.interfaces.rest.controller.client;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.RmaService;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Client Controller pour RMA
 */
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
        var response = rmaService.createRma(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RmaResponse> getRma(@PathVariable Long id) {
        log.info("GET /api/client/rmas/{}", id);
        var response = rmaService.getRmaById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<Page<RmaResponse>> getMyRmas(Pageable pageable, HttpServletRequest request) {
        Long userId = extractUserIdFromToken(request);
        log.info("GET /api/client/rmas/me - User: {}", userId);
        var response = rmaService.getUserRmas(userId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/number/{rmaNumber}")
    public ResponseEntity<RmaResponse> getRmaByNumber(@PathVariable String rmaNumber) {
        log.info("GET /api/client/rmas/number/{}", rmaNumber);
        var response = rmaService.getRmaByNumber(rmaNumber);
        return ResponseEntity.ok(response);
    }

    private Long extractUserIdFromToken(HttpServletRequest request) {
        var authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof cm.dolers.laine_deco.infrastructure.security.AuthenticatedUser user) {
            return user.getId();
        }
        return 1L; // Fallback local
    }
}

