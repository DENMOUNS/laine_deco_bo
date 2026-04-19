package cm.dolers.laine_deco.application.dto;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO pour les paiements
 */
public record PaymentResponse(
    Long id,
    Long orderId,
    BigDecimal amount,
    String method,
    String status,
    String transactionId,
    Instant processedAt
) {}
