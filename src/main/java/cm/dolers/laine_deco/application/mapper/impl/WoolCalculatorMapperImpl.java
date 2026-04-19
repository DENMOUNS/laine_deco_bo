package cm.dolers.laine_deco.application.mapper.impl;


import cm.dolers.laine_deco.application.dto.WoolCalculatorResponse;
import cm.dolers.laine_deco.application.mapper.WoolCalculatorMapper;
import cm.dolers.laine_deco.infrastructure.persistence.entity.WoolCalculationEntity;
import org.springframework.stereotype.Component;

@Component
public class WoolCalculatorMapperImpl implements WoolCalculatorMapper {
    @Override
    public WoolCalculatorResponse toResponse(WoolCalculationEntity calculator) {
        return new WoolCalculatorResponse(calculator.getId(), calculator.getProjectName(),
            calculator.getYarnWeight(), calculator.getNeedleSize(), calculator.getWidth(),
            calculator.getHeight(), calculator.getEstimatedYardage(), calculator.getEstimatedWeight(),
            calculator.getNumberOfSkeins(), calculator.getCreatedAt());
    }
}

