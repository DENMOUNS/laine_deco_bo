package cm.dolers.laine_deco.application.mapper;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.infrastructure.persistence.entity.CouponEntity;
import cm.dolers.laine_deco.infrastructure.persistence.entity.BlogPostEntity;

/**
 * Mapper pour Coupon
 */
public interface CouponMapper {
    CouponResponse toResponse(CouponEntity coupon);
}

/**
 * Mapper pour BlogPost
 */
public interface BlogPostMapper {
    BlogPostResponse toResponse(BlogPostEntity post);
}
