package cm.dolers.laine_deco.application.mapper;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.infrastructure.persistence.entity.ExpenseEntity;

/**
 * Interface Mapper pour Expense
 */
public interface ExpenseMapper {
    ExpenseResponse toResponse(ExpenseEntity expense);
    
    ExpenseEntity createFromRequest(CreateExpenseRequest request);
    
    void updateFromRequest(CreateExpenseRequest request, ExpenseEntity expense);
}
