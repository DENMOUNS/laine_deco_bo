package cm.dolers.laine_deco.application.dto;

import cm.dolers.laine_deco.domain.model.ExpenseCategory;
import cm.dolers.laine_deco.domain.model.PaymentMethod;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateExpenseRequest(
    LocalDate operationDate,
    String description,
    ExpenseCategory category,
    BigDecimal amount,
    PaymentMethod paymentMethod,
    String supplierName,
    String invoiceNumber,
    String createdByName,
    String notes,
    String attachmentPath
) {
    public LocalDate getOperationDate() { return operationDate(); }
    public String getDescription() { return description(); }
    public ExpenseCategory getCategory() { return category(); }
    public BigDecimal getAmount() { return amount(); }
    public PaymentMethod getPaymentMethod() { return paymentMethod(); }
    public String getSupplierName() { return supplierName(); }
    public String getInvoiceNumber() { return invoiceNumber(); }
    public String getCreatedByName() { return createdByName(); }
    public String getNotes() { return notes(); }
    public String getAttachmentPath() { return attachmentPath(); }
}
