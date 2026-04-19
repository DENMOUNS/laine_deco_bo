package cm.dolers.laine_deco.application.mapper;

import cm.dolers.laine_deco.application.dto.CouponResponse;
import cm.dolers.laine_deco.infrastructure.persistence.entity.CouponEntity;

public interface CouponMapper {
    CouponResponse toResponse(CouponEntity coupon);
}

