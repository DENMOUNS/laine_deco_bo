package cm.dolers.laine_deco.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.HashSet;

import java.util.Set;

@Entity
@Table(name = "product_packs")
@NoArgsConstructor
@AllArgsConstructor
public class ProductPackEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private Long salePrice;

    @Column(nullable = false)
    private Integer promotionalDiscount = 0;

    @OneToMany(mappedBy = "productPack", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<ProductPackProductEntity> packProducts = new HashSet<>();

    @Column(columnDefinition = "LONGBLOB")
    private byte[] image;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public java.math.BigDecimal getPrice() {
        return java.math.BigDecimal.valueOf(price);
    }

    public void setPrice(java.math.BigDecimal price) {
        this.price = price != null ? price.longValue() : 0L;
    }

    public java.math.BigDecimal getSalePrice() {
        return java.math.BigDecimal.valueOf(salePrice);
    }

    public void setSalePrice(java.math.BigDecimal salePrice) {
        this.salePrice = salePrice != null ? salePrice.longValue() : 0L;
    }

    public Integer getPromotionalDiscount() {
        return promotionalDiscount;
    }

    public void setPromotionalDiscount(Integer pd) {
        this.promotionalDiscount = pd;
    }

    public Set<ProductPackProductEntity> getPackProducts() {
        return packProducts;
    }

    public void setPackProducts(Set<ProductPackProductEntity> packProducts) {
        this.packProducts = packProducts;
    }

    public String getImage() {
        return image != null ? new String(image) : null;
    }

    public void setImage(String image) {
        this.image = image != null ? image.getBytes() : null;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getTotalProductCount() {
        return packProducts.stream().mapToInt(p -> p.getQuantity()).sum();
    }

    public void setTotalProductCount(int count) {
    }

    public int getUniqueProductCount() {
        return packProducts.size();
    }

    public void setUniqueProductCount(int count) {
    }

    public long getDiscountedPrice() {
        return salePrice - (salePrice * promotionalDiscount / 100);
    }

    public void setDiscountedPrice(long price) {
    }

    public ProductEntity getProduct() {
        return null;
    }
}
