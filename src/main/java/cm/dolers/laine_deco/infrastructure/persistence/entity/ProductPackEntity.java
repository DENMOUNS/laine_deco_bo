package cm.dolers.laine_deco.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product_packs")
@Data
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

    public int getTotalProductCount() {
        return packProducts.stream().mapToInt(p -> p.getQuantity()).sum();
    }

    public int getUniqueProductCount() {
        return packProducts.size();
    }

    public boolean canAddProduct() {
        return packProducts.size() < 4;
    }

    public long getDiscountedPrice() {
        if (promotionalDiscount > 0) {
            return salePrice - (salePrice * promotionalDiscount / 100);
        }
        return salePrice;
    }
}
