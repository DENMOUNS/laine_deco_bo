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
    private Long id;
    private LocalDate operationDate;
    private String description;
    private ExpenseCategory category;
    private BigDecimal amount;
    private ExpenseStatus status;
    private PaymentMethod paymentMethod;
    private String supplierName;
    private String invoiceNumber;
    private String createdByName;
    private String notes;
    private String attachmentPath;
    private Instant createdAt;
    private Instant updatedAt;

    public ExpenseResponse() {
    }

    public ExpenseResponse(Long id, LocalDate operationDate, String description, ExpenseCategory category,
            BigDecimal amount, ExpenseStatus status, PaymentMethod paymentMethod, String supplierName,
            String invoiceNumber, String createdByName, String notes, String attachmentPath, Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.operationDate = operationDate;
        this.description = description;
        this.category = category;
        this.amount = amount;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.supplierName = supplierName;
        this.invoiceNumber = invoiceNumber;
        this.createdByName = createdByName;
        this.notes = notes;
        this.attachmentPath = attachmentPath;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(LocalDate operationDate) {
        this.operationDate = operationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public ExpenseStatus getStatus() {
        return status;
    }

    public void setStatus(ExpenseStatus status) {
        this.status = status;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
