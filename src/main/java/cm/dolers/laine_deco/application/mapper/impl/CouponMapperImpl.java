package cm.dolers.laine_deco.application.mapper.impl;

import cm.dolers.laine_deco.application.dto.CouponResponse;
import cm.dolers.laine_deco.application.mapper.CouponMapper;
import cm.dolers.laine_deco.infrastructure.persistence.entity.CouponEntity;
import org.springframework.stereotype.Component;

@Component
public class CouponMapperImpl implements CouponMapper {

    @Override
    public CouponResponse toResponse(CouponEntity coupon) {
        return new CouponResponse(
            coupon.getId(),
            coupon.getCode(),
            coupon.getType() != null ? coupon.getType().toString() : null,
            coupon.getDescription(),
            coupon.getDiscountAmount(),
            coupon.getDiscountPercentage() != null ? coupon.getDiscountPercentage().doubleValue() : null,
            coupon.getApplicableProductId(),
            null, // applicableCategoryId
            coupon.getUsageLimit(),
            coupon.getUsageCount(),
            coupon.getExpiryDate(),
            coupon.getIsActive(),
            coupon.getCreatedAt()
        );
    }
}


