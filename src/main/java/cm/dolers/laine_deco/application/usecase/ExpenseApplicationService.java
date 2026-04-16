package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.CreateExpenseRequest;
import cm.dolers.laine_deco.application.dto.ExpenseResponse;
import cm.dolers.laine_deco.domain.model.ExpenseCategory;
import cm.dolers.laine_deco.domain.model.ExpenseStatus;
import cm.dolers.laine_deco.infrastructure.persistence.entity.ExpenseEntity;
import cm.dolers.laine_deco.infrastructure.persistence.entity.UserEntity;
import cm.dolers.laine_deco.infrastructure.persistence.repository.ExpenseJpaRepository;
import cm.dolers.laine_deco.infrastructure.persistence.repository.UserJpaRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExpenseApplicationService {
    private final ExpenseJpaRepository expenseRepository;
    private final UserJpaRepository userRepository;

    public ExpenseApplicationService(ExpenseJpaRepository expenseRepository, UserJpaRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ExpenseResponse createExpense(Long userId, CreateExpenseRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        ExpenseEntity expense = new ExpenseEntity();
        expense.setUser(user);
        expense.setOperationDate(request.getOperationDate());
        expense.setDescription(request.getDescription());
        expense.setCategory(request.getCategory());
        expense.setAmount(request.getAmount());
        expense.setPaymentMethod(request.getPaymentMethod());
        expense.setSupplierName(request.getSupplierName());
        expense.setInvoiceNumber(request.getInvoiceNumber());
        expense.setCreatedByName(request.getCreatedByName());
        expense.setNotes(request.getNotes());
        expense.setAttachmentPath(request.getAttachmentPath());
        expense.setStatus(ExpenseStatus.PENDING);

        ExpenseEntity saved = expenseRepository.save(expense);
        return toResponse(saved);
    }

    @Transactional
    public ExpenseResponse updateExpenseStatus(Long expenseId, ExpenseStatus status) {
        ExpenseEntity expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new IllegalArgumentException("Expense not found"));
        expense.setStatus(status);
        expense.setUpdatedAt(Instant.now());
        ExpenseEntity saved = expenseRepository.save(expense);
        return toResponse(saved);
    }

    public List<ExpenseResponse> getUserExpenses(Long userId) {
        return expenseRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ExpenseResponse> getUserExpensesByStatus(Long userId, ExpenseStatus status) {
        return expenseRepository.findByUserIdAndStatus(userId, status).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ExpenseResponse> getUserExpensesByCategory(Long userId, ExpenseCategory category) {
        return expenseRepository.findByUserIdAndCategory(userId, category).stream()
                .map(this::toResponse)
                .toList();
    }

    public BigDecimal getTotalExpensesByStatus(Long userId, ExpenseStatus status) {
        BigDecimal total = expenseRepository.sumByUserIdAndStatus(userId, status);
        return total != null ? total : BigDecimal.ZERO;
    }

    public BigDecimal getTotalExpensesByCategory(Long userId, ExpenseCategory category) {
        BigDecimal total = expenseRepository.sumByUserIdAndCategory(userId, category);
        return total != null ? total : BigDecimal.ZERO;
    }

    private ExpenseResponse toResponse(ExpenseEntity expense) {
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
                expense.getUpdatedAt());
    }
}
