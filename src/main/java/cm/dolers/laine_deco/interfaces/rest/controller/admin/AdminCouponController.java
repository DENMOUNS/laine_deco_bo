package cm.dolers.laine_deco.interfaces.rest.controller.admin;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Admin Controller pour la gestion des coupons/codes promo
 */
@RestController
@RequestMapping("/api/admin/coupons")
@RequiredArgsConstructor

@PreAuthorize("hasRole('ADMIN')")
public class AdminCouponController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AdminCouponController.class);
    private final CouponService couponService;

    @PostMapping
    public ResponseEntity<CouponResponse> createCoupon(
            @Valid @RequestBody CreateCouponRequest request) {
        log.info("POST /api/admin/coupons - Creating: {}", request.code());
        var response = couponService.createCoupon(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CouponResponse> getCoupon(@PathVariable Long id) {
        log.info("GET /api/admin/coupons/{}", id);
        var response = couponService.getCouponById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<CouponResponse> getCouponByCode(@PathVariable String code) {
        log.info("GET /api/admin/coupons/code/{}", code);
        var response = couponService.getCouponByCode(code);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<CouponResponse>> getAllCoupons(Pageable pageable) {
        log.info("GET /api/admin/coupons");
        var response = couponService.getAllCoupons(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    public ResponseEntity<Page<CouponResponse>> getActiveCoupons(Pageable pageable) {
        log.info("GET /api/admin/coupons/active");
        var response = couponService.getActiveCoupons(pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CouponResponse> updateCoupon(
            @PathVariable Long id,
            @Valid @RequestBody CreateCouponRequest request) {
        log.info("PUT /api/admin/coupons/{}", id);
        var response = couponService.updateCoupon(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long id) {
        log.info("DELETE /api/admin/coupons/{}", id);
        couponService.deleteCoupon(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateCoupon(@PathVariable Long id) {
        log.info("POST /api/admin/coupons/{}/deactivate", id);
        couponService.deactivateCoupon(id);
        return ResponseEntity.ok().build();
    }
}
