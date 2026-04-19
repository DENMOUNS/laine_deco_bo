package cm.dolers.laine_deco.application.dto;
public record CreateMaintenanceGuideRequest(String title, String content, String instructions, String image, Boolean isActive, Long categoryId, String brand, Long productId) {
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getInstructions() { return instructions; }
    public String getImage() { return image; }
    public Boolean getIsActive() { return isActive; }
    public Long getCategoryId() { return categoryId; }
    public String getBrand() { return brand; }
    public Long getProductId() { return productId; }
}
