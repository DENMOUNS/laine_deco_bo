package cm.dolers.laine_deco.application.dto;

public record CreatePromoEventRequest(
    String name,
    String description,
    java.math.BigDecimal discountPercentage,
    java.math.BigDecimal discountAmount,
    java.time.Instant startDate,
    java.time.Instant endDate
) {
    public String getName() { return name; }
    public String getDescription() { return description; }
    public java.math.BigDecimal getDiscountPercentage() { return discountPercentage; }
    public java.math.BigDecimal getDiscountAmount() { return discountAmount; }
    public java.time.Instant getStartDate() { return startDate; }
    public java.time.Instant getEndDate() { return endDate; }
}
