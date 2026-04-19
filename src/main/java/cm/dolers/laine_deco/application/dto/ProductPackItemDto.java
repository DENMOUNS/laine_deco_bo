package cm.dolers.laine_deco.application.dto;
public class ProductPackItemDto {
    private Long productId;
    private String productName;
    private long productPrice;
    private Integer quantity;
    private String productImage;
    private String productCategory;
    private String productBrand;

    public Long getProductId() { return productId; } public void setProductId(Long productId) { this.productId = productId; }
    public String getProductName() { return productName; } public void setProductName(String productName) { this.productName = productName; }
    public long getProductPrice() { return productPrice; } public void setProductPrice(long productPrice) { this.productPrice = productPrice; }
    public Integer getQuantity() { return quantity; } public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getProductImage() { return productImage; } public void setProductImage(String productImage) { this.productImage = productImage; }
    public String getProductCategory() { return productCategory; } public void setProductCategory(String productCategory) { this.productCategory = productCategory; }
    public String getProductBrand() { return productBrand; } public void setProductBrand(String productBrand) { this.productBrand = productBrand; }
}
