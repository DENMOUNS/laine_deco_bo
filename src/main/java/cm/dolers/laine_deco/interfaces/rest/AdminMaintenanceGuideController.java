package cm.dolers.laine_deco.interfaces.rest;

import cm.dolers.laine_deco.application.dto.CreateMaintenanceGuideRequest;
import cm.dolers.laine_deco.application.dto.MaintenanceGuideResponse;
import cm.dolers.laine_deco.application.usecase.MaintenanceGuideService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/maintenance-guides")
@RequiredArgsConstructor
public class AdminMaintenanceGuideController {
    private final MaintenanceGuideService maintenanceGuideService;

    @PostMapping
    public ResponseEntity<MaintenanceGuideResponse> createGuide(@RequestBody CreateMaintenanceGuideRequest request) {
        MaintenanceGuideResponse response = maintenanceGuideService.createGuide(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{guideId}")
    public ResponseEntity<MaintenanceGuideResponse> updateGuide(@PathVariable Long guideId, @RequestBody CreateMaintenanceGuideRequest request) {
        MaintenanceGuideResponse response = maintenanceGuideService.updateGuide(guideId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{guideId}")
    public ResponseEntity<MaintenanceGuideResponse> getGuideById(@PathVariable Long guideId) {
        MaintenanceGuideResponse response = maintenanceGuideService.getGuideById(guideId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<MaintenanceGuideResponse>> getAllGuides() {
        List<MaintenanceGuideResponse> guides = maintenanceGuideService.getAllActiveGuides();
        return ResponseEntity.ok(guides);
    }

    @PutMapping("/{guideId}/activate")
    public ResponseEntity<Void> activateGuide(@PathVariable Long guideId) {
        maintenanceGuideService.activateGuide(guideId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{guideId}/deactivate")
    public ResponseEntity<Void> deactivateGuide(@PathVariable Long guideId) {
        maintenanceGuideService.deactivateGuide(guideId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{guideId}")
    public ResponseEntity<Void> deleteGuide(@PathVariable Long guideId) {
        maintenanceGuideService.deleteGuide(guideId);
        return ResponseEntity.noContent().build();
    }
}

