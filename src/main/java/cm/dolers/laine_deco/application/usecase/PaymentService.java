package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.*;
import org.springframework.data.domain.Page;

/**
 * Interface Service pour Paiements
 */
public interface PaymentService {
    PaymentResponse processPayment(CreatePaymentRequest request);
    PaymentResponse getPaymentById(Long paymentId);
    PaymentResponse getPaymentByTransactionId(String transactionId);
    Page<PaymentResponse> getOrderPayments(Long orderId);
    void refundPayment(Long paymentId);
    void validatePayment(Long paymentId);
}

