package cm.dolers.laine_deco.application.dto;

import java.util.List;

/**
 * DTO pour créer une commande (avec ou sans authentification)
 *
 * Règles:
 * - Si userId NULL → pas de loyalty points, coupons annulés
 * - Si userId NOT NULL → loyalty points appliqués
 */
public record CreateOrderRequest(
    List<OrderItemRequest> items,
    DeliveryInfoRequest deliveryInfo,
    String couponCode,
    String paymentMethod,
    String notes
) {}
