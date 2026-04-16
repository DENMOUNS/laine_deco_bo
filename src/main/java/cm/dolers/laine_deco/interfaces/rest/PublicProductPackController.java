package cm.dolers.laine_deco.interfaces.rest;

import cm.dolers.laine_deco.application.dto.ProductPackResponse;
import cm.dolers.laine_deco.application.usecase.ProductPackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/product-packs")
@RequiredArgsConstructor
public class PublicProductPackController {
    private final ProductPackService productPackService;

    @GetMapping
    public ResponseEntity<List<ProductPackResponse>> getAllActivePacks() {
        List<ProductPackResponse> packs = productPackService.getAllActivePacks();
        return ResponseEntity.ok(packs);
    }

    @GetMapping("/{packId}")
    public ResponseEntity<ProductPackResponse> getProductPackById(@PathVariable Long packId) {
        ProductPackResponse response = productPackService.getProductPackById(packId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductPackResponse>> searchProductPacks(@RequestParam String search) {
        List<ProductPackResponse> packs = productPackService.searchPacksByName(search);
        return ResponseEntity.ok(packs);
    }

    @GetMapping("/promotions")
    public ResponseEntity<List<ProductPackResponse>> getPacksWithPromotions() {
        List<ProductPackResponse> packs = productPackService.getPacksWithPromotions();
        return ResponseEntity.ok(packs);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductPackResponse>> getPacksContainingProduct(@PathVariable Long productId) {
        List<ProductPackResponse> packs = productPackService.getPacksContainingProduct(productId);
        return ResponseEntity.ok(packs);
    }
}
