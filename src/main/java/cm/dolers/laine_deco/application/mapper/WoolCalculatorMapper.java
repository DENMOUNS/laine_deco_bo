package cm.dolers.laine_deco.application.mapper;

import cm.dolers.laine_deco.application.dto.WoolCalculatorResponse;
import cm.dolers.laine_deco.infrastructure.persistence.entity.WoolCalculationEntity;

public interface WoolCalculatorMapper {
    WoolCalculatorResponse toResponse(WoolCalculationEntity calculator);
}
