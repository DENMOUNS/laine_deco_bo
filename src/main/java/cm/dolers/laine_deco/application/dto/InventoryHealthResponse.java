package cm.dolers.laine_deco.application.dto;

import java.math.BigDecimal;

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
