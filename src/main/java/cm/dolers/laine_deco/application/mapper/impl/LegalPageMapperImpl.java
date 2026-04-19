package cm.dolers.laine_deco.application.mapper.impl;


import cm.dolers.laine_deco.application.dto.LegalPageResponse;
import cm.dolers.laine_deco.application.mapper.LegalPageMapper;
import cm.dolers.laine_deco.infrastructure.persistence.entity.LegalPageEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class LegalPageMapperImpl implements LegalPageMapper {
    @Override
    public LegalPageResponse toResponse(LegalPageEntity entity) {
        LegalPageResponse response = new LegalPageResponse();
        response.setId(entity.getId());
        response.setType(entity.getType().name());
        response.setTitle(entity.getTitle());
        response.setContent(entity.getContent());
        response.setSummary(entity.getSummary());
        response.setIsPublished(entity.getIsPublished());
        response.setVersion(entity.getVersion() != null ? Integer.valueOf(entity.getVersion()) : null);
        response.setCreatedAt(entity.getCreatedAt().toString());
        response.setUpdatedAt(entity.getUpdatedAt().toString());
        if (entity.getPublishedAt() != null) {
            response.setPublishedAt(entity.getPublishedAt().toString());
        }
        return response;
    }

    @Override
    public List<LegalPageResponse> toResponseList(List<LegalPageEntity> entities) {
        return entities.stream().map(this::toResponse).collect(Collectors.toList());
    }
}


