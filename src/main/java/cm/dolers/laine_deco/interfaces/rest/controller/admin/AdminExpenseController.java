package cm.dolers.laine_deco.interfaces.rest.controller.admin;

import cm.dolers.laine_deco.application.dto.CreateExpenseRequest;
import cm.dolers.laine_deco.application.dto.ExpenseResponse;
import cm.dolers.laine_deco.application.usecase.ExpenseApplicationService;
import cm.dolers.laine_deco.domain.model.ExpenseCategory;
import cm.dolers.laine_deco.domain.model.ExpenseStatus;
import cm.dolers.laine_deco.infrastructure.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * AdminExpenseController
 *
 * CORRECTIONS :
 * - `updateExpenseStatus` : @RequestParam id → @PathVariable id (BUG CRITIQUE)
 * - Utilisation de @AuthenticationPrincipal au lieu de Principal cast manuel
 * - Retour JSON standardisé pour les statistiques
 * - @ResponseStatus sur les méthodes void pour HTTP 204
 */
@RestController
@RequestMapping("/api/finance/expenses")
@PreAuthorize("hasAnyRole('FINANCE', 'ADMIN')")
@RequiredArgsConstructor
public class AdminExpenseController {

    private final ExpenseApplicationService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(
            @RequestBody CreateExpenseRequest request,
            @AuthenticationPrincipal AuthenticatedUser user) {
        ExpenseResponse response = expenseService.createExpense(user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getUserExpenses(
            @AuthenticationPrincipal AuthenticatedUser user) {
        List<ExpenseResponse> expenses = expenseService.getUserExpenses(user.getId());
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/by-status")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByStatus(
            @RequestParam ExpenseStatus status,
            @AuthenticationPrincipal AuthenticatedUser user) {
        List<ExpenseResponse> expenses = expenseService.getUserExpensesByStatus(user.getId(), status);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/by-category")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByCategory(
            @RequestParam ExpenseCategory category,
            @AuthenticationPrincipal AuthenticatedUser user) {
        List<ExpenseResponse> expenses = expenseService.getUserExpensesByCategory(user.getId(), category);
        return ResponseEntity.ok(expenses);
    }

    /**
     * CORRECTION CRITIQUE : `id` était @RequestParam au lieu de @PathVariable
     * → Spring ne trouvait pas le paramètre et levait MissingServletRequestParameterException
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExpenseResponse> updateExpenseStatus(
            @PathVariable Long id,                // CORRIGÉ : PathVariable
            @RequestParam ExpenseStatus status) {
        ExpenseResponse response = expenseService.updateExpenseStatus(id, status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/statistics/total-by-status")
    public ResponseEntity<Map<String, Object>> getTotalByStatus(
            @RequestParam ExpenseStatus status,
            @AuthenticationPrincipal AuthenticatedUser user) {
        var total = expenseService.getTotalExpensesByStatus(user.getId(), status);
        return ResponseEntity.ok(Map.of(
                "total", total,
                "status", status.name(),
                "userId", user.getId()
        ));
    }

    @GetMapping("/statistics/total-by-category")
    public ResponseEntity<Map<String, Object>> getTotalByCategory(
            @RequestParam ExpenseCategory category,
            @AuthenticationPrincipal AuthenticatedUser user) {
        var total = expenseService.getTotalExpensesByCategory(user.getId(), category);
        return ResponseEntity.ok(Map.of(
                "total", total,
                "category", category.name(),
                "userId", user.getId()
        ));
    }
}
