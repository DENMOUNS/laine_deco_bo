package cm.dolers.laine_deco.interfaces.rest.controller.public_api;


import cm.dolers.laine_deco.application.dto.CreateOrderRequest;
import cm.dolers.laine_deco.application.dto.OrderResponse;
import cm.dolers.laine_deco.application.usecase.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Public Controller pour créer des commandes en tant que client anonyme
 * Endpoint: /api/public/orders - POST (créer une commande sans authentification)
 */
@RestController
@RequestMapping("/api/public/orders")
@RequiredArgsConstructor
public class PublicOrderController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PublicOrderController.class);
    private final OrderService orderService;

    /**
     * Créer une commande en tant qu'utilisateur anonyme (guest)
     * 
     * Règles:
     * - Pas d'authentification requise
     * - userId = NULL dans la base
     * - Aucun point de loyauté gagné
     * - Tous les coupons sont annulés
     */
    @PostMapping
    public ResponseEntity<OrderResponse> createGuestOrder(
            @Valid @RequestBody CreateOrderRequest request) {
        log.info("POST /api/public/orders - Guest order");
        var response = orderService.createOrder(null, request); // NULL userId pour guest
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}


