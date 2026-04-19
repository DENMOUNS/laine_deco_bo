package cm.dolers.laine_deco.interfaces.rest;

import cm.dolers.laine_deco.application.dto.CreateProductPackRequest;
import cm.dolers.laine_deco.application.dto.ProductPackResponse;
import cm.dolers.laine_deco.application.usecase.ProductPackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/product-packs")
@RequiredArgsConstructor
public class AdminProductPackController {
    private final ProductPackService productPackService;

    @PostMapping
    public ResponseEntity<ProductPackResponse> createProductPack(
            @RequestBody CreateProductPackRequest request) {
        ProductPackResponse response = productPackService.createProductPack(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{packId}")
    public ResponseEntity<ProductPackResponse> updateProductPack(
            @PathVariable Long packId,
            @RequestBody CreateProductPackRequest request) {
        ProductPackResponse response = productPackService.updateProductPack(packId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{packId}")
    public ResponseEntity<ProductPackResponse> getProductPack(@PathVariable Long packId) {
        ProductPackResponse response = productPackService.getProductPackById(packId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductPackResponse>> getAllProductPacks() {
        List<ProductPackResponse> packs = productPackService.getAllPacks();
        return ResponseEntity.ok(packs);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductPackResponse>> searchProductPacks(
            @RequestParam String search) {
        List<ProductPackResponse> packs = productPackService.searchPacksByName(search);
        return ResponseEntity.ok(packs);
    }

    @PutMapping("/{packId}/deactivate")
    public ResponseEntity<Void> deactivateProductPack(@PathVariable Long packId) {
        productPackService.deactivateProductPack(packId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{packId}/activate")
    public ResponseEntity<Void> activateProductPack(@PathVariable Long packId) {
        productPackService.activateProductPack(packId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{packId}")
    public ResponseEntity<Void> deleteProductPack(@PathVariable Long packId) {
        productPackService.deleteProductPack(packId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{packId}/promotion")
    public ResponseEntity<ProductPackResponse> applyPromotionToPack(
            @PathVariable Long packId,
            @RequestParam Integer discountPercentage) {
        ProductPackResponse response = productPackService.applyPromotionToPack(packId, discountPercentage);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{packId}/promotion")
    public ResponseEntity<ProductPackResponse> removePromotionFromPack(@PathVariable Long packId) {
        ProductPackResponse response = productPackService.removePromotionFromPack(packId);
        return ResponseEntity.ok(response);
    }
}

