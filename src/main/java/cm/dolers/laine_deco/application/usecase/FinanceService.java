package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface Service pour Expenses/Finances
 * Gestion des dépenses et analyses financières
 */
public interface FinanceService {
    /**
     * Créer une dépense
     */
    ExpenseResponse createExpense(CreateExpenseRequest request);
    
    /**
     * Récupérer une dépense
     */
    ExpenseResponse getExpenseById(Long expenseId);
    
    /**
     * Lister les dépenses avec pagination
     */
    Page<ExpenseResponse> getAllExpenses(Pageable pageable);
    
    /**
     * Filtrer par statut
     */
    Page<ExpenseResponse> getExpensesByStatus(String status, Pageable pageable);
    
    /**
     * Filtrer par catégorie
     */
    Page<ExpenseResponse> getExpensesByCategory(String category, Pageable pageable);
    
    /**
     * Filtrer par période
     */
    List<ExpenseResponse> getExpensesByDateRange(LocalDate startDate, LocalDate endDate);
    
    /**
     * Mettre à jour une dépense
     */
    ExpenseResponse updateExpense(Long expenseId, CreateExpenseRequest request);
    
    /**
     * Supprimer une dépense
     */
    void deleteExpense(Long expenseId);
    
    /**
     * Résumé des dépenses
     */
    ExpenseSummaryResponse getExpenseSummary(LocalDate startDate, LocalDate endDate);
    
    /**
     * Dépenses par catégorie
     */
    List<ExpenseByCategoryResponse> getExpensesByCategory(LocalDate startDate, LocalDate endDate);
    
    /**
     * Dépenses par fournisseur
     */
    List<ExpenseBySupplierResponse> getExpensesBySupplier(LocalDate startDate, LocalDate endDate);
}
