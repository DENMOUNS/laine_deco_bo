package cm.dolers.laine_deco.application.dto;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTOs pour les Badges/Achievements
 */
public record BadgeResponse(
    Long id,
    String name,
    String description,
    String icon,
    String criteria,
    Instant createdAt
) {}

public record UserBadgeResponse(
    Long id,
    Long userId,
    Long badgeId,
    String badgeName,
    Instant earnedAt
) {}

public record CreateBadgeRequest(
    String name,
    String description,
    String icon,
    String criteria
) {}

/**
 * DTOs pour les Community Posts
 */
public record CommunityPostResponse(
    Long id,
    Long userId,
    String userName,
    String title,
    String content,
    Integer likesCount,
    Integer commentsCount,
    Integer viewsCount,
    Instant createdAt
) {}

public record CreateCommunityPostRequest(
    String title,
    String content
) {}

public record CommunityCommentResponse(
    Long id,
    Long postId,
    Long userId,
    String userName,
    String comment,
    Integer likesCount,
    Instant createdAt
) {}

/**
 * DTOs pour les Invoices
 */
public record InvoiceResponse(
    Long id,
    Long orderId,
    String invoiceNumber,
    BigDecimal subtotal,
    BigDecimal taxAmount,
    BigDecimal totalAmount,
    String status,
    Instant issuedAt,
    Instant dueDate
) {}

public record CreateInvoiceRequest(
    Long orderId
) {}

/**
 * DTOs pour Wool Calculator
 */
public record WoolCalculatorResponse(
    Long id,
    String projectName,
    String yarnWeight,
    Integer needleSize,
    Integer width,
    Integer height,
    Integer estimatedYardage,
    BigDecimal estimatedWeight,
    Integer numberOfSkeins,
    Instant createdAt
) {}

public record CreateWoolCalculatorRequest(
    String projectName,
    String yarnWeight,
    Integer needleSize,
    Integer width,
    Integer height
) {}

/**
 * DTOs pour Knitting Patterns
 */
public record KnittingPatternResponse(
    Long id,
    String name,
    String author,
    String description,
    String skillLevel,
    Integer estimatedHours,
    String yarnType,
    Integer needleSize,
    String url,
    Integer downloadCount,
    Instant createdAt
) {}

public record CreateKnittingPatternRequest(
    String name,
    String author,
    String description,
    String skillLevel,
    Integer estimatedHours,
    String yarnType,
    Integer needleSize,
    String url
) {}

/**
 * DTOs pour Promo Events
 */
public record PromoEventResponse(
    Long id,
    String name,
    String description,
    Integer discountPercentage,
    BigDecimal discountAmount,
    Instant startDate,
    Instant endDate,
    String status,
    Instant createdAt
) {}

public record CreatePromoEventRequest(
    String name,
    String description,
    Integer discountPercentage,
    BigDecimal discountAmount,
    Instant startDate,
    Instant endDate
) {}

/**
 * DTOs pour RMA (Return Merchandise Authorization)
 */
public record RmaResponse(
    Long id,
    Long orderId,
    String rmaNumber,
    String reason,
    String status,
    String trackingNumber,
    Instant createdAt,
    Instant resolvedAt
) {}

public record CreateRmaRequest(
    Long orderId,
    String reason
) {}

/**
 * DTOs pour Site Configuration
 */
public record SiteConfigResponse(
    Long id,
    String key,
    String value,
    String description,
    Instant updatedAt
) {}

public record UpdateSiteConfigRequest(
    String key,
    String value
) {}
