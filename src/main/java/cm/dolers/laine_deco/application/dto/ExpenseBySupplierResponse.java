package cm.dolers.laine_deco.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO pour les dépenses par fournisseur
 */
public record ExpenseBySupplierResponse(
    String supplierName,
    BigDecimal totalAmount,
    Integer count,
    LocalDate lastExpenseDate
) {}
