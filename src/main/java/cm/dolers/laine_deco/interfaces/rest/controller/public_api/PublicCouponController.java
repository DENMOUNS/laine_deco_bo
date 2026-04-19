package cm.dolers.laine_deco.interfaces.rest.controller.public_api;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.CouponService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/coupons")
@RequiredArgsConstructor

public class PublicCouponController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PublicCouponController.class);
    private final CouponService couponService;

    @GetMapping("/active")
    public ResponseEntity<Page<CouponResponse>> getActiveCoupons(Pageable pageable) {
        log.info("GET /api/public/coupons/active");
        var response = couponService.getActiveCoupons(pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{code}/validate")
    public ResponseEntity<Void> validateCoupon(@PathVariable String code) {
        log.info("POST /api/public/coupons/{}/validate", code);
        couponService.validateCoupon(code);
        return ResponseEntity.ok().build();
    }
}
