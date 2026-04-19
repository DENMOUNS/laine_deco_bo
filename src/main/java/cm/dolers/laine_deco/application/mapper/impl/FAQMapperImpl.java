package cm.dolers.laine_deco.application.mapper.impl;

import cm.dolers.laine_deco.application.dto.FAQResponse;
import cm.dolers.laine_deco.application.mapper.FAQMapper;
import cm.dolers.laine_deco.infrastructure.persistence.entity.FAQEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class FAQMapperImpl implements FAQMapper {
    @Override
    public FAQResponse toResponse(FAQEntity entity) {
        return new FAQResponse(
            entity.getId(),
            entity.getQuestion(),
            entity.getAnswer(),
            entity.getDisplayOrder(),
            entity.getIsActive(),
            entity.getCreatedAt()
        );
    }

    @Override
    public List<FAQResponse> toResponseList(List<FAQEntity> entities) {
        return entities.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
