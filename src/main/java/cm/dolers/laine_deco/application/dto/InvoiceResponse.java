package cm.dolers.laine_deco.application.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record InvoiceResponse(
    Long id,
    Long orderId,
    String invoiceNumber,
    BigDecimal subtotal,
    BigDecimal taxAmount,
    BigDecimal totalAmount,
    String status,
    Instant issuedAt,
    Instant dueDate
) {}
