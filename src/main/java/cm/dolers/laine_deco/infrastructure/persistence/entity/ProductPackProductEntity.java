package cm.dolers.laine_deco.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_pack_products")
@NoArgsConstructor
@AllArgsConstructor
public class ProductPackProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_pack_id", nullable = false)
    private ProductPackEntity productPack;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(nullable = false)
    private Integer quantity;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ProductPackEntity getProductPack() { return productPack; }
    public void setProductPack(ProductPackEntity pp) { this.productPack = pp; }
    public ProductEntity getProduct() { return product; }
    public void setProduct(ProductEntity p) { this.product = p; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer q) { this.quantity = q; }
}
