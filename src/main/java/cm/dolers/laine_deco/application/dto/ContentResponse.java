package cm.dolers.laine_deco.application.dto;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTOs pour les catégories de produits
 */
public record CategoryResponse(
    Long id,
    String name,
    String description,
    Integer productCount,
    Instant createdAt
) {}

public record CreateCategoryRequest(
    String name,
    String description
) {}

/**
 * DTOs pour les coupons/codes promo
 * 
 * Types de coupon:
 * - ALL_PRODUCTS: S'applique à tous les produits du panier
 * - SINGLE_PRODUCT: S'applique à un seul produit (productId spécifié)
 * - FREE_SHIPPING: Offre la livraison gratuite
 * - FIXED_AMOUNT: Déduit un montant fixe de la commande
 */
public record CouponResponse(
    Long id,
    String code,
    String type,                   // ALL_PRODUCTS, SINGLE_PRODUCT, FREE_SHIPPING, FIXED_AMOUNT
    String description,
    BigDecimal discountAmount,     // Montant fixe pour FIXED_AMOUNT
    Integer discountPercentage,    // Pourcentage pour % (ALL_PRODUCTS, SINGLE_PRODUCT)
    Long applicableProductId,      // Non-null si type = SINGLE_PRODUCT
    Integer usageLimit,            // Max nombre d'utilisations
    Integer usageCount,            // Nombre d'utilisations actuelles
    Instant expiryDate,
    Boolean isActive,
    Instant createdAt
) {}

public record CreateCouponRequest(
    String code,
    String type,                   // ALL_PRODUCTS, SINGLE_PRODUCT, FREE_SHIPPING, FIXED_AMOUNT
    String description,
    BigDecimal discountAmount,     // Pour FIXED_AMOUNT
    Integer discountPercentage,    // Pour % discount
    Long applicableProductId,      // Pour SINGLE_PRODUCT
    Integer usageLimit,
    Instant expiryDate
) {}

/**
 * DTOs pour les articles de blog
 */
public record BlogPostResponse(
    Long id,
    String title,
    String slug,
    String content,
    String excerpt,
    String author,
    Integer viewCount,
    Integer likeCount,
    Boolean published,
    Instant publishedAt,
    Instant createdAt
) {}

public record CreateBlogPostRequest(
    String title,
    String content,
    String excerpt,
    String author
) {}
