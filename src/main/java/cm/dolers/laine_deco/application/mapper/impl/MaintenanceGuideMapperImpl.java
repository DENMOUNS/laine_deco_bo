package cm.dolers.laine_deco.application.mapper.impl;

import cm.dolers.laine_deco.application.dto.MaintenanceGuideResponse;
import cm.dolers.laine_deco.application.mapper.MaintenanceGuideMapper;
import cm.dolers.laine_deco.infrastructure.persistence.entity.MaintenanceGuideEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class MaintenanceGuideMapperImpl implements MaintenanceGuideMapper {
    @Override
    public MaintenanceGuideResponse toResponse(MaintenanceGuideEntity entity) {
        MaintenanceGuideResponse response = new MaintenanceGuideResponse();
        response.setId(entity.getId());
        response.setTitle(entity.getTitle());
        response.setContent(entity.getContent());
        response.setInstructions(entity.getInstructions());

        response.setCategoryId(entity.getCategory() != null ? entity.getCategory().getId() : null);
        response.setCategoryName(entity.getCategory() != null ? entity.getCategory().getName() : null);
        response.setBrand(entity.getBrand());
        response.setProductId(entity.getProduct() != null ? entity.getProduct().getId() : null);
        response.setProductName(entity.getProduct() != null ? entity.getProduct().getName() : null);
        response.setIsActive(entity.getIsActive());
        response.setCreatedAt(entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null);
        response.setUpdatedAt(entity.getUpdatedAt() != null ? entity.getUpdatedAt().toString() : null);
        return response;
    }

    @Override
    public List<MaintenanceGuideResponse> toResponseList(List<MaintenanceGuideEntity> entities) {
        return entities.stream().map(this::toResponse).collect(Collectors.toList());
    }
}

