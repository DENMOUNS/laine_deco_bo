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

/**
 * DTO pour les ventes par période
 */
public record SalesAnalyticsResponse(
    LocalDate period,
    Long orderCount,
    BigDecimal revenue,
    BigDecimal expenses,
    BigDecimal profit,
    Double profitMargin
) {}

/**
 * DTO pour les revenus par catégorie
 */
public record CategoryRevenueResponse(
    Long categoryId,
    String categoryName,
    Long productCount,
    BigDecimal revenue,
    Integer salesCount,
    Double percentageOfTotal
) {}

/**
 * DTO pour les top produits
 */
public record TopProductResponse(
    Long productId,
    String productName,
    String sku,
    Integer salesCount,
    BigDecimal revenue,
    Double rating
) {}

/**
 * DTO pour la santé des stocks
 */
public record InventoryHealthResponse(
    Long totalProducts,
    Long inStockProducts,
    Long lowStockProducts,
    Long outOfStockProducts,
    BigDecimal inventoryValue,
    Double stockTurnoverRate
) {}
