package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.CreateMaintenanceGuideRequest;
import cm.dolers.laine_deco.application.dto.MaintenanceGuideResponse;

import java.util.List;

public interface MaintenanceGuideService {
    MaintenanceGuideResponse createGuide(CreateMaintenanceGuideRequest request);
    MaintenanceGuideResponse updateGuide(Long guideId, CreateMaintenanceGuideRequest request);
    MaintenanceGuideResponse getGuideById(Long guideId);
    List<MaintenanceGuideResponse> getAllActiveGuides();
    List<MaintenanceGuideResponse> getGuidesByCategory(Long categoryId);
    List<MaintenanceGuideResponse> getGuidesByBrand(String brand);
    List<MaintenanceGuideResponse> getGuidesByProduct(Long productId);
    List<MaintenanceGuideResponse> searchGuides(String search);
    void activateGuide(Long guideId);
    void deactivateGuide(Long guideId);
    void deleteGuide(Long guideId);
}
