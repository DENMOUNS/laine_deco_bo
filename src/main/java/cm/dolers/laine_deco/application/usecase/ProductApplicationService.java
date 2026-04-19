package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.ProductResponse;
import cm.dolers.laine_deco.infrastructure.persistence.entity.ProductEntity;
import cm.dolers.laine_deco.infrastructure.persistence.repository.ProductJpaRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductApplicationService {
    private final ProductJpaRepository ProductJpaRepository;

    public ProductApplicationService(ProductJpaRepository ProductJpaRepository) {
        this.ProductJpaRepository = ProductJpaRepository;
    }

    @Transactional
    public ProductResponse createProduct(ProductEntity product) {
        ProductEntity saved = ProductJpaRepository.save(product);
        return toResponse(saved);
    }

    @Transactional
    public ProductResponse updateProduct(Long productId, ProductEntity updates) {
        ProductEntity product = ProductJpaRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        if (updates.getName() != null) {
            product.setName(updates.getName());
        }
        if (updates.getDescription() != null) {
            product.setDescription(updates.getDescription());
        }
        if (updates.getSalePrice() != null) {
            product.setSalePrice(updates.getSalePrice());
        }
        if (updates.getCostPrice() != null) {
            product.setCostPrice(updates.getCostPrice());
        }
        if (updates.getStockQuantity() != null) {
            product.setStockQuantity(updates.getStockQuantity());
        }
        if (updates.getReorderLevel() != null) {
            product.setReorderLevel(updates.getReorderLevel());
        }
        ProductEntity saved = ProductJpaRepository.save(product);
        return toResponse(saved);
    }

    public ProductResponse getProduct(Long productId) {
        return ProductJpaRepository.findById(productId)
                .map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }

    public List<ProductResponse> searchProducts(String name) {
        return ProductJpaRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ProductResponse> getAllProducts() {
        return ProductJpaRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ProductResponse> getLowStockProducts() {
        return ProductJpaRepository.findLowStockProducts().stream()
                .map(this::toResponse)
                .toList();
    }

    private ProductResponse toResponse(ProductEntity product) {
        return new ProductResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getDescription(),
                product.getSalePrice(),
                product.getCostPrice(),
                product.getStockQuantity(),
                product.getReorderLevel(),
                product.getMargin(),
                product.getMarginPercentage());
    }

    @Transactional
    public void deactivateProducts(List<Long> productIds) {
        for (Long id : productIds) {
            ProductJpaRepository.findById(id).ifPresent(p -> {
                p.setIsActive(false);
                ProductJpaRepository.save(p);
            });
        }
    }
}


