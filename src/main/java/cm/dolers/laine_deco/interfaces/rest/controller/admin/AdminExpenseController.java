package cm.dolers.laine_deco.interfaces.rest.controller.admin;

import cm.dolers.laine_deco.application.dto.CreateExpenseRequest;
import cm.dolers.laine_deco.application.dto.ExpenseResponse;
import cm.dolers.laine_deco.application.usecase.ExpenseApplicationService;
import cm.dolers.laine_deco.domain.model.ExpenseCategory;
import cm.dolers.laine_deco.domain.model.ExpenseStatus;
import cm.dolers.laine_deco.infrastructure.security.AuthenticatedUser;
import java.security.Principal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/finance/expenses")
@PreAuthorize("hasAnyRole('FINANCE', 'ADMIN')")
public class AdminExpenseController {
    private final ExpenseApplicationService expenseService;

    public AdminExpenseController(ExpenseApplicationService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(@RequestBody CreateExpenseRequest request, Principal principal) {
        AuthenticatedUser user = (AuthenticatedUser) principal;
        ExpenseResponse response = expenseService.createExpense(user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getUserExpenses(Principal principal) {
        AuthenticatedUser user = (AuthenticatedUser) principal;
        List<ExpenseResponse> expenses = expenseService.getUserExpenses(user.getId());
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/by-status")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByStatus(
            @RequestParam ExpenseStatus status,
            Principal principal) {
        AuthenticatedUser user = (AuthenticatedUser) principal;
        List<ExpenseResponse> expenses = expenseService.getUserExpensesByStatus(user.getId(), status);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/by-category")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByCategory(
            @RequestParam ExpenseCategory category,
            Principal principal) {
        AuthenticatedUser user = (AuthenticatedUser) principal;
        List<ExpenseResponse> expenses = expenseService.getUserExpensesByCategory(user.getId(), category);
        return ResponseEntity.ok(expenses);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExpenseResponse> updateExpenseStatus(
            @RequestParam Long id,
            @RequestParam ExpenseStatus status) {
        ExpenseResponse response = expenseService.updateExpenseStatus(id, status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/statistics/total-by-status")
    public ResponseEntity<?> getTotalByStatus(@RequestParam ExpenseStatus status, Principal principal) {
        AuthenticatedUser user = (AuthenticatedUser) principal;
        var total = expenseService.getTotalExpensesByStatus(user.getId(), status);
        return ResponseEntity.ok(java.util.Map.of("total", total, "status", status));
    }

    @GetMapping("/statistics/total-by-category")
    public ResponseEntity<?> getTotalByCategory(@RequestParam ExpenseCategory category, Principal principal) {
        AuthenticatedUser user = (AuthenticatedUser) principal;
        var total = expenseService.getTotalExpensesByCategory(user.getId(), category);
        return ResponseEntity.ok(java.util.Map.of("total", total, "category", category));
    }
}


