package cm.dolers.laine_deco.interfaces.rest.controller.client;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Client Controller pour afficher les commandes
 * Endpoints: Mes commandes, détail commande, annulation
 */
@RestController
@RequestMapping("/api/client/orders")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('CLIENT')")
public class ClientOrderController {
    private final OrderService orderService;

    /**
     * Créer une nouvelle commande
     */
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody CreateOrderRequest request,
            HttpServletRequest httpRequest) {
        Long userId = extractUserIdFromToken(httpRequest);
        log.info("POST /api/client/orders - User: {}", userId);
        var response = orderService.createOrder(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Récupérer mes commandes
     */
    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getMyOrders(
            Pageable pageable,
            HttpServletRequest request) {
        Long userId = extractUserIdFromToken(request);
        log.info("GET /api/client/orders - User: {}", userId);
        var response = orderService.getUserOrders(userId, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Détail d'une commande
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long orderId) {
        log.info("GET /api/client/orders/{}", orderId);
        // TODO: Vérifier que c'est la commande de l'utilisateur connecté
        var response = orderService.getOrderById(orderId);
        return ResponseEntity.ok(response);
    }

    /**
     * Annuler ma commande
     */
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        log.info("POST /api/client/orders/{}/cancel", orderId);
        // TODO: Vérifier que c'est la commande de l'utilisateur connecté
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok().build();
    }

    private Long extractUserIdFromToken(HttpServletRequest request) {
        // TODO: Implémenter correctement l'extraction du token JWT
        return 1L;
    }
}
