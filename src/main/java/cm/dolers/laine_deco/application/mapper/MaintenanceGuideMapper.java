package cm.dolers.laine_deco.application.mapper;

import cm.dolers.laine_deco.application.dto.MaintenanceGuideResponse;
import cm.dolers.laine_deco.infrastructure.persistence.entity.MaintenanceGuideEntity;
import java.util.List;

public interface MaintenanceGuideMapper {
    MaintenanceGuideResponse toResponse(MaintenanceGuideEntity entity);
    List<MaintenanceGuideResponse> toResponseList(List<MaintenanceGuideEntity> entities);
}

