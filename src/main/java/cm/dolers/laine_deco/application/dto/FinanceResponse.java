package cm.dolers.laine_deco.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO pour les statistiques de dépenses
 */
public record ExpenseSummaryResponse(
    BigDecimal totalExpenses,
    BigDecimal averageExpense,
    BigDecimal highestExpense,
    Integer expenseCount,
    LocalDate periodStart,
    LocalDate periodEnd
) {}

/**
 * DTO pour les dépenses par catégorie
 */
public record ExpenseByCategoryResponse(
    String categoryName,
    BigDecimal totalAmount,
    Integer count,
    Double percentageOfTotal
) {}

/**
 * DTO pour les dépenses par fournisseur
 */
public record ExpenseBySupplierResponse(
    String supplierName,
    BigDecimal totalAmount,
    Integer count,
    LocalDate lastExpenseDate
) {}

/**
 * DTO pour l'analyse du profit
 */
public record ProfitAnalysisResponse(
    LocalDate period,
    BigDecimal totalRevenue,
    BigDecimal totalExpenses,
    BigDecimal grossProfit,
    Double profitMargin,
    Double profitGrowth
) {}
