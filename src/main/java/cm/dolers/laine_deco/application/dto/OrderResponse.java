package cm.dolers.laine_deco.application.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * DTO pour la réponse de commande
 * Contient tous les détails: items, livraison, coupon appliqué, statut, etc.
 */
public record OrderResponse(
    Long id,
    Long userId,                           // NULL si commande anonyme
    List<OrderDetailResponse> details,
    DeliveryInfoResponse deliveryInfo,     // Informations de livraison
    BigDecimal subtotal,                   // Prix avant remise
    BigDecimal discountAmount,             // Montant remise (coupon appliqué)
    CouponAppliedResponse couponApplied,   // Details du coupon utilisé (si applicable)
    BigDecimal totalPrice,                 // Prix final (subtotal - discount)
    BigDecimal tax,
    Integer loyaltyPointsEarned,           // Points gagnés (0 si anonyme)
    String status,                         // PENDING, CONFIRMED, etc.
    String paymentMethod,                  // CASH_ON_DELIVERY
    String trackingNumber,
    String notes,
    Instant createdAt,
    Instant updatedAt
) {}

/**
 * Détail d'une ligne de commande
 */
public record OrderDetailResponse(
    Long id,
    Long productId,
    String productName,
    Integer quantity,
    BigDecimal unitPrice,
    BigDecimal subtotal
) {}

/**
 * Informations de livraison complètes
 */
public record DeliveryInfoResponse(
    String firstName,
    String lastName,
    String phone,
    String address,
    String city,
    String district,
    Double latitude,
    Double longitude
) {}

/**
 * Details du coupon appliqué à la commande
 */
public record CouponAppliedResponse(
    String code,
    String type,               // ALL_PRODUCTS, SINGLE_PRODUCT, FREE_SHIPPING, FIXED_AMOUNT
    BigDecimal discountAmount, // Montant déduit
    String descriptionilResponse(
    Long id,
    Long productId,
    String productName,
    Integer quantity,
    BigDecimal unitPrice,
    BigDecimal subtotal
) {}

/**
 * Informations de livraison complètes
 */
public record DeliveryInfoResponse(
    String firstName,
    String lastName,
    String phone,
    String address,
    String city,
    String district,
    Double latitude,
    Double longitude
) {}

/**
 * Details du coupon appliqué à la commande
 */
public record CouponAppliedResponse(
    String code,
    String type,               // ALL_PRODUCTS, SINGLE_PRODUCT, FREE_SHIPPING, FIXED_AMOUNT
    BigDecimal discountAmount, // Montant déduit
    String description
) {}
