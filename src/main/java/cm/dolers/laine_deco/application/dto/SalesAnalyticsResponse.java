package cm.dolers.laine_deco.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO pour les ventes par période
 */
public record SalesAnalyticsResponse(
        LocalDate period,
        Long orderCount,
        BigDecimal revenue,
        BigDecimal expenses,
        BigDecimal profit,
        Double profitMargin) {
}
