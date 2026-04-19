package cm.dolers.laine_deco.application.dto;

import cm.dolers.laine_deco.domain.model.CouponType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

public record CreateCouponRequest(
    @NotBlank(message = "Le code est obligatoire")
    String code,
    @NotNull(message = "Le type est obligatoire")
    CouponType type,
    String description,
    BigDecimal discountAmount,
    Integer discountPercentage,
    Long applicableProductId,
    Boolean isActive,
    @NotNull(message = "La date d'expiration est obligatoire")
    Instant expiryDate,
    Integer usageLimit
) {}

