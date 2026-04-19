package cm.dolers.laine_deco.interfaces.rest.controller.client;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Client Controller pour effectuer les paiements
 */
@RestController
@RequestMapping("/api/client/payments")
@RequiredArgsConstructor

@PreAuthorize("hasRole('CLIENT')")
public class ClientPaymentController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ClientPaymentController.class);
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> processPayment(
            @Valid @RequestBody CreatePaymentRequest request) {
        log.info("POST /api/client/payments - Order: {}", request.orderId());
        var response = paymentService.processPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable Long id) {
        log.info("GET /api/client/payments/{}", id);
        var response = paymentService.getPaymentById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<Page<PaymentResponse>> getOrderPayments(@PathVariable Long orderId) {
        log.info("GET /api/client/payments/orders/{}", orderId);
        var response = paymentService.getOrderPayments(orderId);
        return ResponseEntity.ok(response);
    }
}
