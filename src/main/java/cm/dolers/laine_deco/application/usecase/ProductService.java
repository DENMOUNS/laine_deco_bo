package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Interface Service pour Products
 * Gestion complète du catalogue produits
 */
public interface ProductService {
    ProductDetailResponse createProduct(CreateProductRequest request);
    
    ProductDetailResponse getProductById(Long productId);
    
    ProductDetailResponse updateProduct(Long productId, CreateProductRequest request);
    
    void deleteProduct(Long productId);
    
    List<ProductResponse> searchProducts(String keyword);
    
    List<ProductResponse> getLowStockProducts();
    
    Page<ProductDetailResponse> getProductsByCategory(Long categoryId, Pageable pageable);
    
    List<ProductDetailResponse> getNewProducts(int limit);
    
    void incrementProductViews(Long productId);
}
