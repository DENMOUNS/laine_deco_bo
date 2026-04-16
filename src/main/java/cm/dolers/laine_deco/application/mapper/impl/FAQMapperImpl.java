package cm.dolers.laine_deco.application.mapper.impl;

import cm.dolers.laine_deco.infrastructure.persistence.entity.FAQEntity;
import cm.dolers.laine_deco.application.dto.FAQResponse;
import cm.dolers.laine_deco.application.mapper.FAQMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FAQMapperImpl implements FAQMapper {
    @Override
    public FAQResponse toResponse(FAQEntity entity) {
        FAQResponse response = new FAQResponse();
        response.setId(entity.getId());
        response.setQuestion(entity.getQuestion());
        response.setAnswer(entity.getAnswer());
        response.setDisplayOrder(entity.getDisplayOrder());
        response.setIsActive(entity.getIsActive());
        response.setCreatedAt(entity.getCreatedAt().toString());
        response.setUpdatedAt(entity.getUpdatedAt().toString());
        return response;
    }

    @Override
    public List<FAQResponse> toResponseList(List<FAQEntity> entities) {
        return entities.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
