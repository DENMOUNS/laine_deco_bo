package cm.dolers.laine_deco.application.dto;

public record CreateProductPackRequest(
    String name,
    String description,
    java.math.BigDecimal price,
    java.math.BigDecimal salePrice,
    String image,
    Boolean isActive,
    String startDate,
    String endDate,
    String brand,
    java.util.List<ProductPackItemDto> items
) {
    public String getName() { return name; }
    public String getDescription() { return description; }
    public java.math.BigDecimal getPrice() { return price; }
    public java.math.BigDecimal getSalePrice() { return salePrice; }
    public String getImage() { return image; }
    public Boolean getIsActive() { return isActive; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public String getBrand() { return brand; }
    public java.util.List<ProductPackItemDto> getItems() { return items; }
}
