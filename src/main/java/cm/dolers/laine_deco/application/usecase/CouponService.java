package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interface Service pour Coupons
 */
public interface CouponService {
    CouponResponse createCoupon(CreateCouponRequest request);
    CouponResponse getCouponById(Long couponId);
    CouponResponse getCouponByCode(String code);
    Page<CouponResponse> getAllCoupons(Pageable pageable);
    Page<CouponResponse> getActiveCoupons(Pageable pageable);
    CouponResponse updateCoupon(Long couponId, CreateCouponRequest request);
    void deleteCoupon(Long couponId);
    void deactivateCoupon(Long couponId);
    void validateCoupon(String code);
    void incrementUsage(String code);
}

