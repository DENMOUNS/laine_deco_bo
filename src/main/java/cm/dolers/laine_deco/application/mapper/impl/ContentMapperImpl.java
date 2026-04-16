package cm.dolers.laine_deco.application.mapper.impl;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.mapper.CouponMapper;
import cm.dolers.laine_deco.application.mapper.BlogPostMapper;
import cm.dolers.laine_deco.infrastructure.persistence.entity.CouponEntity;
import cm.dolers.laine_deco.infrastructure.persistence.entity.BlogPostEntity;
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
            coupon.getDiscountPercentage(),
            coupon.getApplicableProductId(),
            coupon.getUsageLimit(),
            coupon.getUsageCount(),
            coupon.getExpiryDate(),
            coupon.getIsActive(),
            coupon.getCreatedAt()
        );
    }
}

@Component
class BlogPostMapperImpl implements BlogPostMapper {

    @Override
    public BlogPostResponse toResponse(BlogPostEntity post) {
        return new BlogPostResponse(
            post.getId(),
            post.getTitle(),
            post.getSlug(),
            post.getContent(),
            post.getExcerpt(),
            post.getAuthor(),
            post.getViewCount(),
            post.getLikeCount(),
            post.getPublished(),
            post.getPublishedAt(),
            post.getCreatedAt()
        );
    }
}
