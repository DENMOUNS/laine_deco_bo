package cm.dolers.laine_deco.application.usecase.impl;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.mapper.CouponMapper;
import cm.dolers.laine_deco.application.usecase.CouponService;
import cm.dolers.laine_deco.domain.exception.ErrorCode;
import cm.dolers.laine_deco.domain.exception.ValidationException;
import cm.dolers.laine_deco.infrastructure.persistence.entity.CouponEntity;
import cm.dolers.laine_deco.infrastructure.persistence.repository.CouponJpaRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor

public class CouponServiceImpl implements CouponService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CouponServiceImpl.class);
    private final CouponJpaRepository couponRepository;
    private final CouponMapper couponMapper;

    @Override
    @Transactional
    public CouponResponse createCoupon(CreateCouponRequest request) {
        log.info("Creating coupon: {}", request.code());

        if (couponRepository.existsByCode(request.code())) {
            throw new ValidationException(ErrorCode.COUPON_CODE_DUPLICATE, "Code: " + request.code());
        }

        try {
            var coupon = new CouponEntity();
            coupon.setCode(request.code());
            coupon.setType(request.type());
            coupon.setDescription(request.description());
            coupon.setDiscountAmount(request.discountAmount());
            coupon.setDiscountPercentage(request.discountPercentage());
            coupon.setApplicableProductId(request.applicableProductId());
            coupon.setUsageLimit(request.usageLimit());
            coupon.setExpiryDate(request.expiryDate());
            coupon.setIsActive(true);
            coupon.setUsageCount(0);

            var saved = couponRepository.save(coupon);
            log.info("Coupon created: {}", saved.getId());
            return couponMapper.toResponse(saved);
        } catch (Exception ex) {
            log.error("Error creating coupon", ex);
            throw new ValidationException(ErrorCode.OPERATION_FAILED, "Error creating coupon", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CouponResponse getCouponById(Long couponId) {
        var coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new ValidationException(ErrorCode.COUPON_NOT_FOUND, "ID: " + couponId));
        return couponMapper.toResponse(coupon);
    }

    @Override
    @Transactional(readOnly = true)
    public CouponResponse getCouponByCode(String code) {
        var coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new ValidationException(ErrorCode.COUPON_NOT_FOUND, "Code: " + code));
        return couponMapper.toResponse(coupon);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CouponResponse> getAllCoupons(Pageable pageable) {
        return couponRepository.findAll(pageable)
                .map(couponMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CouponResponse> getActiveCoupons(Pageable pageable) {
        return couponRepository.findByIsActiveTrueAndExpiryDateAfter(Instant.now(), pageable)
                .map(couponMapper::toResponse);
    }

    @Override
    @Transactional
    public CouponResponse updateCoupon(Long couponId, CreateCouponRequest request) {
        var coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new ValidationException(ErrorCode.COUPON_NOT_FOUND, "ID: " + couponId));

        coupon.setCode(request.code());
        coupon.setType(request.type());
        coupon.setDescription(request.description());
        coupon.setDiscountAmount(request.discountAmount());
        coupon.setDiscountPercentage(request.discountPercentage());
        coupon.setApplicableProductId(request.applicableProductId());
        coupon.setUsageLimit(request.usageLimit());
        coupon.setExpiryDate(request.expiryDate());

        var updated = couponRepository.save(coupon);
        log.info("Coupon updated: {}", couponId);
        return couponMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteCoupon(Long couponId) {
        if (!couponRepository.existsById(couponId)) {
            throw new ValidationException(ErrorCode.COUPON_NOT_FOUND, "ID: " + couponId);
        }
        couponRepository.deleteById(couponId);
        log.info("Coupon deleted: {}", couponId);
    }

    @Override
    @Transactional
    public void deactivateCoupon(Long couponId) {
        var coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new ValidationException(ErrorCode.COUPON_NOT_FOUND, "ID: " + couponId));
        coupon.setIsActive(false);
        couponRepository.save(coupon);
        log.info("Coupon deactivated: {}", couponId);
    }

    @Override
    @Transactional(readOnly = true)
    public void validateCoupon(String code) {
        var coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new ValidationException(ErrorCode.COUPON_NOT_FOUND, "Code: " + code));

        if (!coupon.getIsActive()) {
            throw new ValidationException(ErrorCode.COUPON_INACTIVE, "Coupon is inactive");
        }

        if (coupon.getExpiryDate().isBefore(Instant.now())) {
            throw new ValidationException(ErrorCode.COUPON_EXPIRED, "Coupon has expired");
        }

        if (coupon.getUsageCount() >= coupon.getUsageLimit()) {
            throw new ValidationException(ErrorCode.COUPON_LIMIT_EXCEEDED, "Usage limit exceeded");
        }
    }

    @Override
    @Transactional
    public void incrementUsage(String code) {
        var coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new ValidationException(ErrorCode.COUPON_NOT_FOUND, "Code: " + code));
        coupon.setUsageCount(coupon.getUsageCount() + 1);
        couponRepository.save(coupon);
        log.info("Coupon usage incremented: {}", code);
    }
}
