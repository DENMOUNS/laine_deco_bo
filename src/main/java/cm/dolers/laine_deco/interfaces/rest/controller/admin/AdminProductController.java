package cm.dolers.laine_deco.interfaces.rest.controller.admin;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Admin Controller pour la gestion des produits
 * Endpoints: CRUD produits, stock, catégories
 */
@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'FINANCE')")
public class AdminProductController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AdminProductController.class);
    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDetailResponse> createProduct(
            @Valid @RequestBody CreateProductRequest request) {
        log.info("POST /api/admin/products - Creating product: {}", request.sku());
        var response = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailResponse> getProduct(@PathVariable Long id) {
        log.info("GET /api/admin/products/{}", id);
        var response = productService.getProductById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDetailResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody CreateProductRequest request) {
        log.info("PUT /api/admin/products/{}", id);
        var response = productService.updateProduct(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("DELETE /api/admin/products/{}", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<java.util.List<ProductResponse>> searchProducts(
            @RequestParam String keyword) {
        log.info("GET /api/admin/products/search - Keyword: {}", keyword);
        var results = productService.searchProducts(keyword);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/stock/low")
    public ResponseEntity<java.util.List<ProductResponse>> getLowStockProducts() {
        log.info("GET /api/admin/products/stock/low");
        var results = productService.getLowStockProducts();
        return ResponseEntity.ok(results);
    }
}


