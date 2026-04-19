package cm.dolers.laine_deco.application.mapper;


import cm.dolers.laine_deco.application.dto.KnittingPatternResponse;
import cm.dolers.laine_deco.infrastructure.persistence.entity.PatternEntity;

public interface KnittingPatternMapper {
    KnittingPatternResponse toResponse(PatternEntity pattern);
}

