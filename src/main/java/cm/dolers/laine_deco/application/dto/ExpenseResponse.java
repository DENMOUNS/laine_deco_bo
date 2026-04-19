package cm.dolers.laine_deco.application.dto;

import cm.dolers.laine_deco.domain.model.ExpenseCategory;
import cm.dolers.laine_deco.domain.model.ExpenseStatus;
import cm.dolers.laine_deco.domain.model.PaymentMethod;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Record DTO pour les réponses de dépenses (Java 17+)
 * Les Records sont immutables et genèrent automatiquement tous les accesseurs
 */
public record ExpenseResponse(
    Long id,
    LocalDate operationDate,
    String description,
    ExpenseCategory category,
    BigDecimal amount,
    ExpenseStatus status,
    PaymentMethod paymentMethod,
    String supplierName,
    String invoiceNumber,
    String createdByName,
    String notes,
    String attachmentPath,
    Instant createdAt,
    Instant updatedAt
) {}

