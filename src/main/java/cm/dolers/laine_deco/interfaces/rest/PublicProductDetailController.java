package cm.dolers.laine_deco.interfaces.rest;

import cm.dolers.laine_deco.application.dto.ProductDetailWithSimilarsResponse;
import cm.dolers.laine_deco.application.usecase.ProductService;
import cm.dolers.laine_deco.application.usecase.SimilarProductsService;
import cm.dolers.laine_deco.application.mapper.ProductMapper;
import cm.dolers.laine_deco.infrastructure.persistence.entity.ProductEntity;
import cm.dolers.laine_deco.infrastructure.persistence.repository.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoint public pour les détails d'un produit avec produits similaires
 */
@RestController
@RequestMapping("/api/public/products")
@RequiredArgsConstructor
@Slf4j
public class PublicProductDetailController {
    private final ProductService productService;
    private final SimilarProductsService similarProductsService;
    private final ProductJpaRepository productRepository;
    private final ProductMapper productMapper;

    /**
     * GET /api/public/products/:id/details
     * Récupère les détails d'un produit avec 3 à 5 produits similaires
     */
    @GetMapping("/{id}/details")
    public ResponseEntity<ProductDetailWithSimilarsResponse> getProductDetails(@PathVariable Long id) {
        log.info("GET /api/public/products/{}/details - Fetching product details with similar products", id);

        // Récupérer le produit
        ProductEntity productEntity = productRepository.findById(id)
            .orElse(null);

        if (productEntity == null) {
            log.warn("Product not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Incrémenter les vues
        productService.incrementProductViews(id);

        // Récupérer les détails du produit
        var productDetail = productService.getProductById(id);

        // Récupérer les produits similaires
        List<ProductEntity> similarProducts = similarProductsService.findSimilarProducts(id, productEntity);

        // Mapper les produits similaires
        var similarProductResponses = similarProducts.stream()
            .map(productMapper::toResponse)
            .toList();

        log.info("Found {} similar products for productId: {}", similarProductResponses.size(), id);

        var response = new ProductDetailWithSimilarsResponse(productDetail, similarProductResponses);

        return ResponseEntity.ok(response);
    }
}
