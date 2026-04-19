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
