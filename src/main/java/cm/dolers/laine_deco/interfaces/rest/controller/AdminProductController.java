package cm.dolers.laine_deco.interfaces.rest.controller;

import cm.dolers.laine_deco.application.usecase.ProductServiceWithLombok;
import cm.dolers.laine_deco.domain.exception.ApplicationException;
import cm.dolers.laine_deco.domain.exception.ErrorCode;
import cm.dolers.laine_deco.infrastructure.config.PaginationConstants;
import cm.dolers.laine_deco.infrastructure.persistence.entity.ProductEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasAnyRole('ADMIN', 'FINANCE')")
public class AdminProductController {
    private final ProductServiceWithLombok productService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductEntity product) {
        try {
            log.info("Creating product: {}", product.getName());
            ProductEntity created = productService.createProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (ApplicationException ex) {
            log.error("Application error creating product: {}", ex.getErrorCode().getCode());
            throw ex;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        try {
            ProductEntity product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (ApplicationException ex) {
            log.warn("Product not found: {}", id);
            throw ex;
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            // Normaliser la taille de page
            int normalizedSize = PaginationConstants.normalizePageSize(pageSize);
            
            log.info("Searching products by name: {} - page: {}, size: {}", name, page, normalizedSize);
            List<ProductEntity> results = productService.searchProducts(name);
            
            // Appliquer la pagination manuellement
            int start = page * normalizedSize;
            int end = Math.min(start + normalizedSize, results.size());
            List<ProductEntity> pageContent = results.subList(start, end);
            
            Page<ProductEntity> pageResult = new PageImpl<>(
                pageContent,
                org.springframework.data.domain.PageRequest.of(page, normalizedSize),
                results.size()
            );
            
            return ResponseEntity.ok(pageResult);
        } catch (ApplicationException ex) {
            throw ex;
        }
    }

    @PutMapping("/{id}/stock/decrement")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> decrementStock(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        try {
            if (quantity == null || quantity <= 0) {
                throw new ApplicationException(ErrorCode.INVALID_REQUEST, 
                    "Quantity must be greater than 0");
            }
            productService.decrementStock(id, quantity);
            return ResponseEntity.ok(new Response(true, "Stock decremented successfully"));
        } catch (ApplicationException ex) {
            throw ex;
        }
    }

    // Classe utilitaire pour les réponses
    @lombok.Getter
    @lombok.Setter
    @lombok.AllArgsConstructor
    public static class Response {
        private boolean success;
        private String message;
    }
}
