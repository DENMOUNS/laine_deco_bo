package cm.dolers.laine_deco.application.dto;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO pour la réponse de promotion
 */
public record PromotionResponse(
    Long id,
    String name,
    String description,
    String type,
    Instant startDate,
    Instant endDate,
    Boolean isActive,
    BigDecimal discountPercentage,
    BigDecimal discountAmount,
    Long categoryId,
    String categoryName,
    String brand,
    Long productId,
    String productName,
    Long timeRemainingMs,
    String timeRemainingFormatted
) {}
