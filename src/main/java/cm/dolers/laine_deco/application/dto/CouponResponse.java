package cm.dolers.laine_deco.application.dto;

public record CouponResponse(
    Long id,
    String code,
    String type,
    String description,
    java.math.BigDecimal discountAmount,
    Double discountPercentage,
    Long applicableProductId,
    Long applicableCategoryId,
    Integer usageLimit,
    Integer usageCount,
    java.time.Instant expiryDate,
    Boolean isActive,
    java.time.Instant createdAt
) {}
