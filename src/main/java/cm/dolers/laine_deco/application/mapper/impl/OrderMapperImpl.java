package cm.dolers.laine_deco.application.mapper.impl;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.mapper.OrderMapper;
import cm.dolers.laine_deco.infrastructure.persistence.entity.OrderDetailEntity;
import cm.dolers.laine_deco.infrastructure.persistence.entity.OrderEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderResponse toResponse(OrderEntity order) {
        var details = order.getDetails().stream()
            .map(this::toDetailResponse)
            .toList();

        // CrÃ©er le DTO pour les infos de livraison
        DeliveryInfoResponse deliveryInfo = new DeliveryInfoResponse(
            order.getDeliveryFirstName(),
            order.getDeliveryLastName(),
            order.getDeliveryPhone(),
            order.getDeliveryAddress(),
            order.getDeliveryCity(),
            order.getDeliveryDistrict(),
            order.getDeliveryLatitude(),
            order.getDeliveryLongitude()
        );

        // CrÃ©er le DTO pour le coupon appliquÃ©
        CouponAppliedResponse couponApplied = order.getCouponCode() != null
            ? new CouponAppliedResponse(
                order.getCouponCode(),
                order.getCouponType(),
                order.getDiscountAmount(),
                null // On n'a pas la description stockÃ©e dans OrderEntity
            )
            : null;

        return new OrderResponse(
            order.getId(),
            order.getUser() != null ? order.getUser().getId() : null,
            details,
            deliveryInfo,
            order.getSubtotal(),
            order.getDiscountAmount(),
            couponApplied,
            order.getTotal(),
            order.getTaxAmount(),
            order.getLoyaltyPointsEarned(),
            order.getStatus() != null ? order.getStatus().toString() : "PENDING",
            order.getPaymentMethod() != null ? order.getPaymentMethod() : "CASH_ON_DELIVERY",
            order.getTrackingNumber(),
            order.getNotes(),
            order.getCreatedAt(),
            order.getUpdatedAt()
        );
    }

    @Override
    public OrderDetailResponse toDetailResponse(OrderDetailEntity detail) {
        return new OrderDetailResponse(
            detail.getId(),
            detail.getProduct().getId(),
            detail.getProduct().getName(),
            detail.getQuantity(),
            detail.getPrice(),  // unitPrice
            detail.getPrice().multiply(java.math.BigDecimal.valueOf(detail.getQuantity()))
        );
    }
}
