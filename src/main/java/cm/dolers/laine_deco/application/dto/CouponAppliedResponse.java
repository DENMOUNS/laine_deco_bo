package cm.dolers.laine_deco.application.dto;

import java.math.BigDecimal;

/**
 * Détails du coupon appliqué à la commande
 */
public record CouponAppliedResponse(
    String code,
    String type,
    BigDecimal discountAmount,
    String description
) {}
