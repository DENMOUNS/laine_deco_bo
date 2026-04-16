package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.domain.exception.ApplicationException;
import cm.dolers.laine_deco.domain.exception.ErrorCode;
import cm.dolers.laine_deco.infrastructure.persistence.entity.ProductEntity;
import cm.dolers.laine_deco.infrastructure.persistence.repository.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceWithLombok {
    private final ProductJpaRepository productRepository;

    @Transactional
    public ProductEntity createProduct(ProductEntity product) {
        if (product.getSku() == null || product.getSku().isEmpty()) {
            throw new ApplicationException(ErrorCode.PRODUCT_SKU_EXISTS, "SKU ne peut pas être vide");
        }

        if (productRepository.findBySku(product.getSku()).isPresent()) {
            throw new ApplicationException(ErrorCode.PRODUCT_SKU_EXISTS, "SKU: " + product.getSku());
        }

        if (product.getSalePrice() == null || product.getSalePrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ApplicationException(ErrorCode.PRODUCT_INVALID_PRICE);
        }

        try {
            ProductEntity saved = productRepository.save(product);
            log.info("Product created successfully: {}", saved.getId());
            return saved;
        } catch (Exception ex) {
            log.error("Error creating product: {}", product.getSku(), ex);
            throw new ApplicationException(ErrorCode.OPERATION_FAILED, "Erreur création produit", ex);
        }
    }

    public ProductEntity getProductById(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> {
                log.warn("Product not found: {}", productId);
                return new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND, "ID: " + productId);
            });
    }

    public List<ProductEntity> searchProducts(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ApplicationException(ErrorCode.INVALID_REQUEST, "Le critère de recherche ne peut pas être vide");
        }

        List<ProductEntity> results = productRepository.findByNameContainingIgnoreCase(name);
        log.debug("Search found {} products for: {}", results.size(), name);
        return results;
    }

    @Transactional
    public void decrementStock(Long productId, Integer quantity) {
        ProductEntity product = getProductById(productId);

        if (product.getStockQuantity() < quantity) {
            log.warn("Stock insufficient for product {}: requested={}, available={}", 
                productId, quantity, product.getStockQuantity());
            throw new ApplicationException(ErrorCode.PRODUCT_OUT_OF_STOCK, 
                "Quantité disponible: " + product.getStockQuantity());
        }

        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);
        log.info("Stock decremented for product {}: quantity={}", productId, quantity);
    }
}
