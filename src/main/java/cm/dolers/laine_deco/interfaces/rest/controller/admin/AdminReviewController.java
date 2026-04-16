package cm.dolers.laine_deco.interfaces.rest.controller.admin;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Admin Controller pour la modération des avis
 */
@RestController
@RequestMapping("/api/admin/reviews")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasAnyRole('ADMIN')")
public class AdminReviewController {
    private final ReviewService reviewService;

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> getReview(@PathVariable Long reviewId) {
        log.info("GET /api/admin/reviews/{}", reviewId);
        var response = reviewService.getReviewById(reviewId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{reviewId}/approve")
    public ResponseEntity<Void> approveReview(@PathVariable Long reviewId) {
        log.info("POST /api/admin/reviews/{}/approve", reviewId);
        reviewService.approveReview(reviewId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{reviewId}/reject")
    public ResponseEntity<Void> rejectReview(@PathVariable Long reviewId) {
        log.info("POST /api/admin/reviews/{}/reject", reviewId);
        reviewService.rejectReview(reviewId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        log.info("DELETE /api/admin/reviews/{}", reviewId);
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<Page<ReviewResponse>> getProductReviews(
            @PathVariable Long productId,
            Pageable pageable) {
        log.info("GET /api/admin/reviews/product/{}", productId);
        var response = reviewService.getProductReviews(productId, pageable);
        return ResponseEntity.ok(response);
    }
}
