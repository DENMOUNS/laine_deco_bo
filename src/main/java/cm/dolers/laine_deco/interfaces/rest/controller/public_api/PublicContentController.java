package cm.dolers.laine_deco.interfaces.rest.controller.public_api;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.CategoryService;
import cm.dolers.laine_deco.application.usecase.CouponService;
import cm.dolers.laine_deco.application.usecase.BlogPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Public Controller pour voir les catégories
 */
@RestController
@RequestMapping("/api/public/categories")
@RequiredArgsConstructor
@Slf4j
public class PublicCategoryController {
    private final CategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable Long id) {
        log.info("GET /api/public/categories/{}", id);
        var response = categoryService.getCategoryById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<CategoryResponse>> getAllCategories(Pageable pageable) {
        log.info("GET /api/public/categories");
        var response = categoryService.getAllCategories(pageable);
        return ResponseEntity.ok(response);
    }
}

/**
 * Public Controller pour voir les coupons actifs
 */
@RestController
@RequestMapping("/api/public/coupons")
@RequiredArgsConstructor
class PublicCouponController {
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

/**
 * Public Controller pour voir les articles de blog publiés
 */
@RestController
@RequestMapping("/api/public/blog")
@RequiredArgsConstructor
class PublicBlogPostController {
    private final BlogPostService blogPostService;

    @GetMapping("/posts/{id}")
    public ResponseEntity<BlogPostResponse> getBlogPost(@PathVariable Long id) {
        log.info("GET /api/public/blog/posts/{}", id);
        var response = blogPostService.getBlogPostById(id);
        blogPostService.incrementViewCount(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/posts/slug/{slug}")
    public ResponseEntity<BlogPostResponse> getBlogPostBySlug(@PathVariable String slug) {
        log.info("GET /api/public/blog/posts/slug/{}", slug);
        var response = blogPostService.getBlogPostBySlug(slug);
        blogPostService.incrementViewCount(response.id());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/posts")
    public ResponseEntity<Page<BlogPostResponse>> getPublishedPosts(Pageable pageable) {
        log.info("GET /api/public/blog/posts");
        var response = blogPostService.getPublishedPosts(pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/posts/{id}/like")
    public ResponseEntity<Void> likeBlogPost(@PathVariable Long id) {
        log.info("POST /api/public/blog/posts/{}/like", id);
        blogPostService.incrementLikeCount(id);
        return ResponseEntity.ok().build();
    }
}
