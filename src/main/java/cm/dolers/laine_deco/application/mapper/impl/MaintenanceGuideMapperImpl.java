package cm.dolers.laine_deco.application.mapper.impl;

import cm.dolers.laine_deco.infrastructure.persistence.entity.MaintenanceGuideEntity;
import cm.dolers.laine_deco.application.dto.MaintenanceGuideResponse;
import cm.dolers.laine_deco.application.mapper.MaintenanceGuideMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MaintenanceGuideMapperImpl implements MaintenanceGuideMapper {
    @Override
    public MaintenanceGuideResponse toResponse(MaintenanceGuideEntity entity) {
        MaintenanceGuideResponse response = new MaintenanceGuideResponse();
        response.setId(entity.getId());
        response.setTitle(entity.getTitle());
        response.setContent(entity.getContent());
        response.setInstructions(entity.getInstructions());
        response.setScope(entity.getScope());
        response.setImage(entity.getImage());
        response.setIsActive(entity.getIsActive());
        response.setCreatedAt(entity.getCreatedAt().toString());
        response.setUpdatedAt(entity.getUpdatedAt().toString());
        
        if (entity.getCategory() != null) {
            response.setCategoryId(entity.getCategory().getId());
            response.setCategoryName(entity.getCategory().getName());
        }
        if (entity.getBrand() != null) {
            response.setBrand(entity.getBrand());
        }
        if (entity.getProduct() != null) {
            response.setProductId(entity.getProduct().getId());
            response.setProductName(entity.getProduct().getName());
        }
        return response;
    }

    @Override
    public List<MaintenanceGuideResponse> toResponseList(List<MaintenanceGuideEntity> entities) {
        return entities.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
