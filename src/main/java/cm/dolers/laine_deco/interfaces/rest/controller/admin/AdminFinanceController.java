package cm.dolers.laine_deco.interfaces.rest.controller.admin;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.FinanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Admin Controller pour la gestion des dépenses/finances
 * Endpoints: CRUD dépenses, analyses, rapports
 */
@RestController
@RequestMapping("/api/admin/finances")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasAnyRole('ADMIN', 'FINANCE')")
public class AdminFinanceController {
    private final FinanceService financeService;

    @PostMapping("/expenses")
    @PreAuthorize("hasRole('FINANCE')")
    public ResponseEntity<ExpenseResponse> createExpense(
            @Valid @RequestBody CreateExpenseRequest request) {
        log.info("POST /api/admin/finances/expenses - Creating expense: {}", request.description());
        var response = financeService.createExpense(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/expenses/{id}")
    public ResponseEntity<ExpenseResponse> getExpense(@PathVariable Long id) {
        log.info("GET /api/admin/finances/expenses/{}", id);
        var response = financeService.getExpenseById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/expenses")
    public ResponseEntity<Page<ExpenseResponse>> getAllExpenses(Pageable pageable) {
        log.info("GET /api/admin/finances/expenses");
        var response = financeService.getAllExpenses(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/expenses/status/{status}")
    public ResponseEntity<Page<ExpenseResponse>> getExpensesByStatus(
            @PathVariable String status,
            Pageable pageable) {
        log.info("GET /api/admin/finances/expenses/status/{}", status);
        var response = financeService.getExpensesByStatus(status, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/expenses/category/{category}")
    public ResponseEntity<Page<ExpenseResponse>> getExpensesByCategory(
            @PathVariable String category,
            Pageable pageable) {
        log.info("GET /api/admin/finances/expenses/category/{}", category);
        var response = financeService.getExpensesByCategory(category, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/expenses/date-range")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        log.info("GET /api/admin/finances/expenses/date-range from {} to {}", startDate, endDate);
        var response = financeService.getExpensesByDateRange(startDate, endDate);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/expenses/{id}")
    @PreAuthorize("hasRole('FINANCE')")
    public ResponseEntity<ExpenseResponse> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody CreateExpenseRequest request) {
        log.info("PUT /api/admin/finances/expenses/{}", id);
        var response = financeService.updateExpense(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/expenses/{id}")
    @PreAuthorize("hasRole('FINANCE')")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        log.info("DELETE /api/admin/finances/expenses/{}", id);
        financeService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary")
    public ResponseEntity<ExpenseSummaryResponse> getExpenseSummary(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        log.info("GET /api/admin/finances/summary from {} to {}", startDate, endDate);
        var response = financeService.getExpenseSummary(startDate, endDate);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-category")
    public ResponseEntity<List<ExpenseByCategoryResponse>> getExpensesByCategory(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        log.info("GET /api/admin/finances/by-category from {} to {}", startDate, endDate);
        var response = financeService.getExpensesByCategory(startDate, endDate);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-supplier")
    public ResponseEntity<List<ExpenseBySupplierResponse>> getExpensesBySupplier(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        log.info("GET /api/admin/finances/by-supplier from {} to {}", startDate, endDate);
        var response = financeService.getExpensesBySupplier(startDate, endDate);
        return ResponseEntity.ok(response);
    }
}
