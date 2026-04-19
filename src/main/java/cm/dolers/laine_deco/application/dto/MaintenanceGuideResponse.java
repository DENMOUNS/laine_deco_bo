package cm.dolers.laine_deco.application.dto;

public class MaintenanceGuideResponse {
    private Long id;
    private String title;
    private String content;
    private String instructions;
    private String scope;
    private Long categoryId;
    private String categoryName;
    private String brand;
    private Long productId;
    private String productName;
    private byte[] image;
    private Boolean isActive;
    private String createdAt;
    private String updatedAt;

    public MaintenanceGuideResponse() {}

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; } public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; } public void setContent(String content) { this.content = content; }
    public String getInstructions() { return instructions; } public void setInstructions(String instructions) { this.instructions = instructions; }
    public String getScope() { return scope; } public void setScope(String scope) { this.scope = scope; }
    public Long getCategoryId() { return categoryId; } public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public String getCategoryName() { return categoryName; } public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getBrand() { return brand; } public void setBrand(String brand) { this.brand = brand; }
    public Long getProductId() { return productId; } public void setProductId(Long productId) { this.productId = productId; }
    public String getProductName() { return productName; } public void setProductName(String productName) { this.productName = productName; }
    public byte[] getImage() { return image; } public void setImage(byte[] image) { this.image = image; }
    public Boolean getIsActive() { return isActive; } public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public String getCreatedAt() { return createdAt; } public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getUpdatedAt() { return updatedAt; } public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
