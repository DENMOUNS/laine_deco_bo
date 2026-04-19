package cm.dolers.laine_deco.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO pour les statistiques générales du dashboard
 */
public record DashboardStatsResponse(
    Long totalUsers,
    Long totalOrders,
    BigDecimal totalRevenue,
    BigDecimal totalExpenses,
    BigDecimal netProfit,
    Double averageOrderValue,
    Double conversionRate,
    Long lowStockProducts,
    Long pendingReviews,
    Integer totalProducts,
    LocalDate dateRange
) {}
