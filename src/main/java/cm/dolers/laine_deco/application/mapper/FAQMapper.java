package cm.dolers.laine_deco.application.mapper;


import cm.dolers.laine_deco.application.dto.FAQResponse;
import cm.dolers.laine_deco.infrastructure.persistence.entity.FAQEntity;
import java.util.List;

public interface FAQMapper {
    FAQResponse toResponse(FAQEntity entity);
    List<FAQResponse> toResponseList(List<FAQEntity> entities);
}

