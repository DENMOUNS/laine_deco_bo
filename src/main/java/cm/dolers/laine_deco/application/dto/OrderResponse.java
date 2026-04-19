package cm.dolers.laine_deco.application.dto;


import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * DTO pour la réponse de commande
 */
public record OrderResponse(
    Long id,
    Long userId,
    List<OrderDetailResponse> details,
    DeliveryInfoResponse deliveryInfo,
    BigDecimal subtotal,
    BigDecimal discountAmount,
    CouponAppliedResponse couponApplied,
    BigDecimal totalPrice,
    BigDecimal tax,
    Integer loyaltyPointsEarned,
    String status,
    String paymentMethod,
    String trackingNumber,
    String notes,
    Instant createdAt,
    Instant updatedAt
) {}
