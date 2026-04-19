package cm.dolers.laine_deco.application.mapper.impl;

import cm.dolers.laine_deco.application.dto.KnittingPatternResponse;
import cm.dolers.laine_deco.application.mapper.KnittingPatternMapper;
import cm.dolers.laine_deco.infrastructure.persistence.entity.PatternEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class KnittingPatternMapperImpl implements KnittingPatternMapper {
    @Override
    public KnittingPatternResponse toResponse(PatternEntity entity) {
        return new KnittingPatternResponse(
            entity.getId(),
            entity.getName(),
            null, // slug
            entity.getDescription(),
            null, // price
            entity.getSkillLevel() != null ? entity.getSkillLevel().name() : null,
            null, // format
            null, // tags
            true, // isActive
            entity.getCreatedAt()
        );
    }
    public List<KnittingPatternResponse> toResponseList(List<PatternEntity> entities) {
        return entities.stream().map(this::toResponse).collect(Collectors.toList());
    }
}


