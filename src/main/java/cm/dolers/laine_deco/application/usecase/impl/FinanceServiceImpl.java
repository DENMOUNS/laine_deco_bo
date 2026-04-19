package cm.dolers.laine_deco.application.usecase.impl;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.mapper.ExpenseMapper;
import cm.dolers.laine_deco.application.usecase.FinanceService;
import cm.dolers.laine_deco.domain.exception.ErrorCode;
import cm.dolers.laine_deco.domain.exception.ExpenseException;
import cm.dolers.laine_deco.domain.model.ExpenseStatus;
import cm.dolers.laine_deco.infrastructure.persistence.entity.ExpenseEntity;
import cm.dolers.laine_deco.infrastructure.persistence.repository.ExpenseJpaRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class FinanceServiceImpl implements FinanceService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FinanceServiceImpl.class);
    private final ExpenseJpaRepository expenseRepository;
    private final ExpenseMapper expenseMapper;

    @Override
    @Transactional
    public ExpenseResponse createExpense(CreateExpenseRequest request) {
        log.info("Creating expense: {}", request.description());

        if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ExpenseException(ErrorCode.EXPENSE_INVALID_AMOUNT);
        }

        try {
            var expense = expenseMapper.createFromRequest(request);
            expense.setStatus(ExpenseStatus.PENDING);
            var saved = expenseRepository.save(expense);
            log.info("Expense created: {}", saved.getId());
            return expenseMapper.toResponse(saved);
        } catch (Exception ex) {
            log.error("Error creating expense", ex);
            throw new ExpenseException(ErrorCode.OPERATION_FAILED, "Error creating expense", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ExpenseResponse getExpenseById(Long expenseId) {
        var expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ExpenseException(ErrorCode.EXPENSE_NOT_FOUND, "ID: " + expenseId));
        return expenseMapper.toResponse(expense);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExpenseResponse> getAllExpenses(Pageable pageable) {
        return expenseRepository.findAll(pageable)
                .map(expenseMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExpenseResponse> getExpensesByStatus(String status, Pageable pageable) {
        try {
            var expenseStatus = ExpenseStatus.valueOf(status.toUpperCase());
            return expenseRepository.findByStatus(expenseStatus, pageable)
                    .map(expenseMapper::toResponse);
        } catch (IllegalArgumentException ex) {
            throw new ExpenseException(ErrorCode.INVALID_REQUEST, "Invalid status: " + status);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExpenseResponse> getExpensesByCategory(String category, Pageable pageable) {
        try {
            var expenseCategory = cm.dolers.laine_deco.domain.model.ExpenseCategory.valueOf(category.toUpperCase());
            return expenseRepository.findByCategory(expenseCategory, pageable)
                    .map(expenseMapper::toResponse);
        } catch (IllegalArgumentException ex) {
            throw new ExpenseException(ErrorCode.INVALID_REQUEST, "Invalid category: " + category);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseResponse> getExpensesByDateRange(LocalDate startDate, LocalDate endDate) {
        return expenseRepository.findByOperationDateBetween(startDate, endDate)
                .stream()
                .map(expenseMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ExpenseResponse updateExpense(Long expenseId, CreateExpenseRequest request) {
        var expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ExpenseException(ErrorCode.EXPENSE_NOT_FOUND, "ID: " + expenseId));

        expenseMapper.updateFromRequest(request, expense);
        var updated = expenseRepository.save(expense);
        log.info("Expense updated: {}", expenseId);
        return expenseMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteExpense(Long expenseId) {
        if (!expenseRepository.existsById(expenseId)) {
            throw new ExpenseException(ErrorCode.EXPENSE_NOT_FOUND, "ID: " + expenseId);
        }
        expenseRepository.deleteById(expenseId);
        log.info("Expense deleted: {}", expenseId);
    }

    @Override
    @Transactional(readOnly = true)
    public ExpenseSummaryResponse getExpenseSummary(LocalDate startDate, LocalDate endDate) {
        var expenses = getExpensesByDateRange(startDate, endDate);

        BigDecimal total = expenses.stream()
                .map(ExpenseResponse::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal average = expenses.isEmpty()
                ? BigDecimal.ZERO
                : total.divide(BigDecimal.valueOf(expenses.size()), 2, RoundingMode.HALF_UP);

        BigDecimal highest = expenses.stream()
                .map(ExpenseResponse::amount)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        return new ExpenseSummaryResponse(total, average, highest, expenses.size(), startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseByCategoryResponse> getExpensesByCategory(LocalDate startDate, LocalDate endDate) {
        var expenses = getExpensesByDateRange(startDate, endDate);
        BigDecimal total = expenses.stream()
                .map(ExpenseResponse::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return expenses.stream()
                .collect(Collectors.groupingBy(ExpenseResponse::category))
                .entrySet().stream()
                .map(entry -> {
                    BigDecimal categoryTotal = entry.getValue().stream()
                            .map(ExpenseResponse::amount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    double percentage = total.compareTo(BigDecimal.ZERO) > 0
                            ? categoryTotal.divide(total, 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100))
                                    .doubleValue()
                            : 0;

                    return new ExpenseByCategoryResponse(
                            entry.getKey().toString(),
                            categoryTotal,
                            entry.getValue().size(),
                            percentage);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseBySupplierResponse> getExpensesBySupplier(LocalDate startDate, LocalDate endDate) {
        return expenseRepository.findByOperationDateBetween(startDate, endDate).stream()
                .collect(Collectors.groupingBy(ExpenseEntity::getSupplierName))
                .entrySet().stream()
                .map(entry -> new ExpenseBySupplierResponse(
                        entry.getKey(),
                        entry.getValue().stream()
                                .map(ExpenseEntity::getAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add),
                        entry.getValue().size(),
                        entry.getValue().stream()
                                .map(ExpenseEntity::getOperationDate)
                                .max(LocalDate::compareTo)
                                .orElse(null)))
                .collect(Collectors.toList());
    }
}
