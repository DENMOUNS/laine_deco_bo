package cm.dolers.laine_deco.application.dto;

import java.math.BigDecimal;

/**
 * DTO pour créer un paiement
 */
public record CreatePaymentRequest(
    Long orderId,
    BigDecimal amount,
    String method
) {}
