package cm.dolers.laine_deco.interfaces.rest;

import cm.dolers.laine_deco.application.dto.MaintenanceGuideResponse;
import cm.dolers.laine_deco.application.usecase.MaintenanceGuideService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/maintenance-guides")
@RequiredArgsConstructor
public class PublicMaintenanceGuideController {
    private final MaintenanceGuideService maintenanceGuideService;

    @GetMapping
    public ResponseEntity<List<MaintenanceGuideResponse>> getAllGuides() {
        List<MaintenanceGuideResponse> guides = maintenanceGuideService.getAllActiveGuides();
        return ResponseEntity.ok(guides);
    }

    @GetMapping("/{guideId}")
    public ResponseEntity<MaintenanceGuideResponse> getGuideById(@PathVariable Long guideId) {
        MaintenanceGuideResponse response = maintenanceGuideService.getGuideById(guideId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<MaintenanceGuideResponse>> getGuidesByCategory(@PathVariable Long categoryId) {
        List<MaintenanceGuideResponse> guides = maintenanceGuideService.getGuidesByCategory(categoryId);
        return ResponseEntity.ok(guides);
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<MaintenanceGuideResponse>> getGuidesByBrand(@PathVariable String brand) {
        List<MaintenanceGuideResponse> guides = maintenanceGuideService.getGuidesByBrand(brand);
        return ResponseEntity.ok(guides);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<MaintenanceGuideResponse>> getGuidesByProduct(@PathVariable Long productId) {
        List<MaintenanceGuideResponse> guides = maintenanceGuideService.getGuidesByProduct(productId);
        return ResponseEntity.ok(guides);
    }

    @GetMapping("/search")
    public ResponseEntity<List<MaintenanceGuideResponse>> searchGuides(@RequestParam String search) {
        List<MaintenanceGuideResponse> guides = maintenanceGuideService.searchGuides(search);
        return ResponseEntity.ok(guides);
    }
}
