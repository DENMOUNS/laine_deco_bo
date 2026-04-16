package cm.dolers.laine_deco.application.mapper.impl;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.mapper.ExpenseMapper;
import cm.dolers.laine_deco.infrastructure.persistence.entity.ExpenseEntity;
import org.springframework.stereotype.Component;

@Component
public class ExpenseMapperImpl implements ExpenseMapper {

    @Override
    public ExpenseResponse toResponse(ExpenseEntity expense) {
        return new ExpenseResponse(
            expense.getId(),
            expense.getOperationDate(),
            expense.getDescription(),
            expense.getCategory(),
            expense.getAmount(),
            expense.getStatus(),
            expense.getPaymentMethod(),
            expense.getSupplierName(),
            expense.getInvoiceNumber(),
            expense.getCreatedByName(),
            expense.getNotes(),
            expense.getAttachmentPath(),
            expense.getCreatedAt(),
            expense.getUpdatedAt()
        );
    }

    @Override
    public ExpenseEntity createFromRequest(CreateExpenseRequest request) {
        var expense = new ExpenseEntity();
        updateFromRequest(request, expense);
        return expense;
    }

    @Override
    public void updateFromRequest(CreateExpenseRequest request, ExpenseEntity expense) {
        expense.setOperationDate(request.operationDate());
        expense.setDescription(request.description());
        expense.setCategory(request.category());
        expense.setAmount(request.amount());
        expense.setPaymentMethod(request.paymentMethod());
        expense.setSupplierName(request.supplierName());
        expense.setInvoiceNumber(request.invoiceNumber());
        expense.setNotes(request.notes());
        expense.setAttachmentPath(request.attachmentPath());
    }
}
