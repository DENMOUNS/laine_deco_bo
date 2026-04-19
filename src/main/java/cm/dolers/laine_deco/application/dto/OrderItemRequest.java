package cm.dolers.laine_deco.application.dto;

import java.math.BigDecimal;

/**
 * Item de commande (Product + Quantity + Price)
 */
public record OrderItemRequest(
    Long productId,
    Integer quantity,
    BigDecimal unitPrice
) {}
