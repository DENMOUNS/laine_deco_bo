package cm.dolers.laine_deco.application.dto;
import java.math.BigDecimal;
import java.util.List;
public class ProductPackResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal salePrice;
    private long discountedPrice;
    private long promotionalDiscount;
    private String image;
    private Boolean isActive;
    private String createdAt;
    private String updatedAt;
    private int totalProductCount;
    private int uniqueProductCount;
    private List<ProductPackItemDto> items;

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public String getDescription() { return description; } public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; } public void setPrice(BigDecimal price) { this.price = price; }
    public BigDecimal getSalePrice() { return salePrice; } public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }
    public long getDiscountedPrice() { return discountedPrice; } public void setDiscountedPrice(long discountedPrice) { this.discountedPrice = discountedPrice; }
    public long getPromotionalDiscount() { return promotionalDiscount; } public void setPromotionalDiscount(long promotionalDiscount) { this.promotionalDiscount = promotionalDiscount; }
    public String getImage() { return image; } public void setImage(String image) { this.image = image; }
    public Boolean getIsActive() { return isActive; } public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public String getCreatedAt() { return createdAt; } public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getUpdatedAt() { return updatedAt; } public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    public int getTotalProductCount() { return totalProductCount; } public void setTotalProductCount(int totalProductCount) { this.totalProductCount = totalProductCount; }
    public int getUniqueProductCount() { return uniqueProductCount; } public void setUniqueProductCount(int uniqueProductCount) { this.uniqueProductCount = uniqueProductCount; }
    public List<ProductPackItemDto> getItems() { return items; } public void setItems(List<ProductPackItemDto> items) { this.items = items; }
}
