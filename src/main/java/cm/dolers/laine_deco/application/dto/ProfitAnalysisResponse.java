package cm.dolers.laine_deco.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

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
