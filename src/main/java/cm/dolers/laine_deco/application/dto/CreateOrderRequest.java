package cm.dolers.laine_deco.application.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO pour créer une commande (avec ou sans authentification)
 * 
 * Règles:
 * - Si userId NULL → pas de loyalty points, coupons annulés
 * - Si userId NOT NULL → loyalty points appliqués
 */
public record CreateOrderRequest(
    List<OrderItemRequest> items,          // Produits et quantités
    DeliveryInfoRequest deliveryInfo,      // Informations de livraison
    String couponCode,                      // Code coupon (optionnel)
    String paymentMethod,                   // CASH_ON_DELIVERY
    String notes                            // Notes (optionnel)
) {}

/**
 * Item de commande (Product + Quantity + Price)
 */
public record OrderItemRequest(
    Long productId,
    Integer quantity,
    BigDecimal unitPrice
) {}

/**
 * Informations de livraison
 */
public record DeliveryInfoRequest(
    String firstName,
    String lastName,
    String phone,
    String address,
    String city,
    String district,
    Double latitude,    // Optionnel
    Double longitude    // Optionnel
) {}

