package cm.dolers.laine_deco.interfaces.rest.controller.client;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.ProductService;
import cm.dolers.laine_deco.application.usecase.SimilarProductsService;
import cm.dolers.laine_deco.application.mapper.ProductMapper;
import cm.dolers.laine_deco.infrastructure.config.PaginationConstants;
import cm.dolers.laine_deco.infrastructure.persistence.entity.ProductEntity;
import cm.dolers.laine_deco.infrastructure.persistence.repository.ProductJpaRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Client Controller pour consulter le catalogue de produits
 * Endpoints: Voir produits, liste par catégorie, nouveautés
 */
@RestController
@RequestMapping("/api/client/products")
@RequiredArgsConstructor

@PreAuthorize("hasRole('CLIENT')")
public class ClientProductController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ClientProductController.class);
    private final ProductService productService;
    private final SimilarProductsService similarProductsService;
    private final ProductJpaRepository ProductJpaRepository;
    private final ProductMapper productMapper;

    /**
     * Consulter un produit
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailResponse> getProduct(@PathVariable Long id) {
        log.info("GET /api/client/products/{}", id);
        productService.incrementProductViews(id);
        var response = productService.getProductById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Chercher des produits
     */
    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponse>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        log.info("GET /api/client/products/search - Keyword: {}, page: {}, size: {}", keyword, page, pageSize);

        int normalizedSize = PaginationConstants.normalizePageSize(pageSize);
        List<ProductResponse> results = productService.searchProducts(keyword);

        // Appliquer la pagination manuellement
        int start = page * normalizedSize;
        int end = Math.min(start + normalizedSize, results.size());
        List<ProductResponse> pageContent = results.subList(start, end);

        Page<ProductResponse> pageResult = new PageImpl<>(
                pageContent,
                org.springframework.data.domain.PageRequest.of(page, normalizedSize),
                results.size());

        return ResponseEntity.ok(pageResult);
    }

    /**
     * Voir les produits par catégorie
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ProductDetailResponse>> getByCategory(
            @PathVariable Long categoryId,
            Pageable pageable) {
        log.info("GET /api/client/products/category/{}", categoryId);
        var results = productService.getProductsByCategory(categoryId, pageable);
        return ResponseEntity.ok(results);
    }

    /**
     * Voir les nouveaux produits
     */
    @GetMapping("/new")
    public ResponseEntity<Page<ProductDetailResponse>> getNewProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        log.info("GET /api/client/products/new - page: {}, size: {}", page, pageSize);

        int normalizedSize = PaginationConstants.normalizePageSize(pageSize);
        List<ProductDetailResponse> results = productService.getNewProducts(Integer.MAX_VALUE);

        // Appliquer la pagination manuellement
        int start = page * normalizedSize;
        int end = Math.min(start + normalizedSize, results.size());
        List<ProductDetailResponse> pageContent = results.subList(start, end);

        Page<ProductDetailResponse> pageResult = new PageImpl<>(
                pageContent,
                org.springframework.data.domain.PageRequest.of(page, normalizedSize),
                results.size());

        return ResponseEntity.ok(pageResult);
    }

    /**
     * Détails d'un produit avec produits similaires (3-5)
     */
    @GetMapping("/{id}/with-similars")
    public ResponseEntity<ProductDetailWithSimilarsResponse> getProductWithSimilars(@PathVariable Long id) {
        log.info("GET /api/client/products/{}/with-similars", id);

        var productEntity = ProductJpaRepository.findById(id).orElse(null);
        if (productEntity == null) {
            return ResponseEntity.notFound().build();
        }

        productService.incrementProductViews(id);
        var productDetail = productService.getProductById(id);

        List<ProductEntity> similarProducts = similarProductsService.findSimilarProducts(id, productEntity);
        var similarResponses = similarProducts.stream()
                .map(productMapper::toResponse)
                .toList();

        return ResponseEntity.ok(new ProductDetailWithSimilarsResponse(productDetail, similarResponses));
    }
}
