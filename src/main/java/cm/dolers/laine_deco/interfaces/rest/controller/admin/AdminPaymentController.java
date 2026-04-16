package cm.dolers.laine_deco.interfaces.rest.controller.admin;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Admin Controller pour la gestion des paiements
 */
@RestController
@RequestMapping("/api/admin/payments")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasAnyRole('ADMIN', 'FINANCE')")
public class AdminPaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> processPayment(
            @Valid @RequestBody CreatePaymentRequest request) {
        log.info("POST /api/admin/payments - Processing for order: {}", request.orderId());
        var response = paymentService.processPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable Long id) {
        log.info("GET /api/admin/payments/{}", id);
        var response = paymentService.getPaymentById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<PaymentResponse> getByTransactionId(@PathVariable String transactionId) {
        log.info("GET /api/admin/payments/transaction/{}", transactionId);
        var response = paymentService.getPaymentByTransactionId(transactionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<Page<PaymentResponse>> getOrderPayments(@PathVariable Long orderId) {
        log.info("GET /api/admin/payments/order/{}", orderId);
        var response = paymentService.getOrderPayments(orderId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/refund")
    @PreAuthorize("hasRole('FINANCE')")
    public ResponseEntity<Void> refundPayment(@PathVariable Long id) {
        log.info("POST /api/admin/payments/{}/refund", id);
        paymentService.refundPayment(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validatePayment(@PathVariable Long id) {
        log.info("POST /api/admin/payments/{}/validate", id);
        paymentService.validatePayment(id);
        return ResponseEntity.ok().build();
    }
}
