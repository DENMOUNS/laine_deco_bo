package cm.dolers.laine_deco.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_pack_products", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"product_pack_id", "product_id"})
})
@Data
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

    public ProductPackProductEntity(ProductEntity product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }
}
