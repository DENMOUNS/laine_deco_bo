package cm.dolers.laine_deco.application.usecase.impl;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.mapper.PaymentMapper;
import cm.dolers.laine_deco.application.usecase.PaymentService;;
import cm.dolers.laine_deco.domain.exception.ErrorCode;
import cm.dolers.laine_deco.domain.exception.ValidationException;
import cm.dolers.laine_deco.infrastructure.persistence.entity.*;
import cm.dolers.laine_deco.infrastructure.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentJpaRepository paymentRepository;
    private final OrderJpaRepository orderRepository;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public PaymentResponse processPayment(CreatePaymentRequest request) {
        log.info("Processing payment for order: {}", request.orderId());

        var order = orderRepository.findById(request.orderId())
            .orElseThrow(() -> new ValidationException(ErrorCode.ORDER_NOT_FOUND, "ID: " + request.orderId()));

        if (request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException(ErrorCode.PAYMENT_INVALID_AMOUNT, "Amount must be > 0");
        }

        try {
            var payment = new PaymentEntity();
            payment.setOrder(order);
            payment.setAmount(request.amount());
            payment.setMethod(request.method());
            payment.setStatus("PENDING");
            payment.setTransactionId(generateTransactionId());

            // TODO: Intégrer avec passerelle paiement réelle
            payment.setStatus("SUCCESS");

            var saved = paymentRepository.save(payment);
            log.info("Payment processed: {}", saved.getId());
            return paymentMapper.toResponse(saved);
        } catch (Exception ex) {
            log.error("Error processing payment", ex);
            throw new ValidationException(ErrorCode.PAYMENT_FAILED, "Error processing payment", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(Long paymentId) {
        var payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new ValidationException(ErrorCode.PAYMENT_NOT_FOUND, "ID: " + paymentId));
        return paymentMapper.toResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByTransactionId(String transactionId) {
        var payment = paymentRepository.findByTransactionId(transactionId)
            .orElseThrow(() -> new ValidationException(ErrorCode.PAYMENT_NOT_FOUND, "Transaction: " + transactionId));
        return paymentMapper.toResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentResponse> getOrderPayments(Long orderId) {
        var payments = paymentRepository.findByOrderId(orderId);
        var responses = payments.stream()
            .map(paymentMapper::toResponse)
            .toList();
        return new PageImpl<>(responses, PageRequest.of(0, 10), responses.size());
    }

    @Override
    @Transactional
    public void refundPayment(Long paymentId) {
        var payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new ValidationException(ErrorCode.PAYMENT_NOT_FOUND, "ID: " + paymentId));

        if ("REFUNDED".equals(payment.getStatus())) {
            throw new ValidationException(ErrorCode.PAYMENT_ALREADY_REFUNDED, "Already refunded");
        }

        payment.setStatus("REFUNDED");
        paymentRepository.save(payment);
        log.info("Payment refunded: {}", paymentId);
    }

    @Override
    @Transactional
    public void validatePayment(Long paymentId) {
        var payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new ValidationException(ErrorCode.PAYMENT_NOT_FOUND, "ID: " + paymentId));

        if (!"SUCCESS".equals(payment.getStatus())) {
            throw new ValidationException(ErrorCode.PAYMENT_NOT_SUCCESS, "Payment status is: " + payment.getStatus());
        }
        log.info("Payment validated: {}", paymentId);
    }

    private String generateTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
    }
}
