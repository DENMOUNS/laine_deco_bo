package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Interface Service pour Catégories
 */
public interface CategoryService {
    CategoryResponse createCategory(CreateCategoryRequest request);
    CategoryResponse getCategoryById(Long categoryId);
    Page<CategoryResponse> getAllCategories(Pageable pageable);
    List<CategoryResponse> getAllCategories();
    CategoryResponse updateCategory(Long categoryId, CreateCategoryRequest request);
    void deleteCategory(Long categoryId);
    CategoryResponse getCategoryByName(String name);
}

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

/**
 * Interface Service pour Posts de Blog
 */
public interface BlogPostService {
    BlogPostResponse createBlogPost(CreateBlogPostRequest request);
    BlogPostResponse getBlogPostById(Long postId);
    BlogPostResponse getBlogPostBySlug(String slug);
    Page<BlogPostResponse> getAllPosts(Pageable pageable);
    Page<BlogPostResponse> getPublishedPosts(Pageable pageable);
    BlogPostResponse updateBlogPost(Long postId, CreateBlogPostRequest request);
    void deleteBlogPost(Long postId);
    void publishPost(Long postId);
    void incrementViewCount(Long postId);
    void incrementLikeCount(Long postId);
}
