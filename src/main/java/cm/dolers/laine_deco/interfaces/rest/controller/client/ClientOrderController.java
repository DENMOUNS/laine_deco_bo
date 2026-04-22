package cm.dolers.laine_deco.interfaces.rest.controller.client;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.OrderService;
import cm.dolers.laine_deco.infrastructure.security.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Client Controller pour afficher les commandes
 * Endpoints: Mes commandes, détail commande, annulation
 */
@RestController
@RequestMapping("/api/client/orders")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CLIENT')")
public class ClientOrderController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ClientOrderController.class);
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        log.info("POST /api/client/orders - User: {}", userId);
        var response = orderService.createOrder(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getMyOrders(Pageable pageable) {
        Long userId = SecurityUtils.getCurrentUserId();
        log.info("GET /api/client/orders - User: {}", userId);
        var response = orderService.getUserOrders(userId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long orderId) {
        log.info("GET /api/client/orders/{}", orderId);
        Long userId = SecurityUtils.getCurrentUserId();
        var response = orderService.getOrderById(orderId);

        if (!userId.equals(response.userId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès refusé");
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        log.info("POST /api/client/orders/{}/cancel", orderId);
        Long userId = SecurityUtils.getCurrentUserId();
        var response = orderService.getOrderById(orderId);

        if (!userId.equals(response.userId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès refusé");
        }

        orderService.cancelOrder(orderId);
        return ResponseEntity.ok().build();
    }
}
