package cm.dolers.laine_deco.interfaces.rest.controller.public_api;

import cm.dolers.laine_deco.application.dto.CouponResponse;
import cm.dolers.laine_deco.application.usecase.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Public Controller pour Coupons/Codes Promo
 * Responsabilité: Exposer les coupons actifs au public et valider les codes
 */
@RestController
@RequestMapping("/api/public/coupons")
@RequiredArgsConstructor
@Slf4j
public class PublicCouponController {
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
