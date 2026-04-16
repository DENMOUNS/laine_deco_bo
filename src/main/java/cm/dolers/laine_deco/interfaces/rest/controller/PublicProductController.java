package cm.dolers.laine_deco.interfaces.rest.controller;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Public Controller pour voir les produits sans authentification
 * Utile pour le catalogue accessible à tous
 */
@RestController
@RequestMapping("/api/public/products")
@RequiredArgsConstructor
@Slf4j
public class PublicProductController {
    private final ProductService productService;

    /**
     * Consulter un produit (public)
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailResponse> getProduct(@PathVariable Long id) {
        log.info("GET /api/public/products/{} (PUBLIC)", id);
        productService.incrementProductViews(id);
        var response = productService.getProductById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Chercher des produits (public)
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(
            @RequestParam String keyword) {
        log.info("GET /api/public/products/search (PUBLIC) - Keyword: {}", keyword);
        var results = productService.searchProducts(keyword);
        return ResponseEntity.ok(results);
    }

    /**
     * Voir les produits par catégorie (public)
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ProductDetailResponse>> getByCategory(
            @PathVariable Long categoryId,
            Pageable pageable) {
        log.info("GET /api/public/products/category/{} (PUBLIC)", categoryId);
        var results = productService.getProductsByCategory(categoryId, pageable);
        return ResponseEntity.ok(results);
    }

    /**
     * Voir les nouveaux produits (public)
     */
    @GetMapping("/new")
    public ResponseEntity<List<ProductDetailResponse>> getNewProducts(
            @RequestParam(defaultValue = "10") int limit) {
        log.info("GET /api/public/products/new (PUBLIC) - Limit: {}", limit);
        var results = productService.getNewProducts(limit);
        return ResponseEntity.ok(results);
    }
}
