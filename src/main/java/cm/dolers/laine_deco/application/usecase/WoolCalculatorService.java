package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service pour Wool Calculator
 */
public interface WoolCalculatorService {
    WoolCalculatorResponse calculateWool(CreateWoolCalculatorRequest request);
    WoolCalculatorResponse saveCalculation(CreateWoolCalculatorRequest request);
    WoolCalculatorResponse getCalculationById(Long calculationId);
    Page<WoolCalculatorResponse> getUserCalculations(Long userId, Pageable pageable);
    void deleteCalculation(Long calculationId);
}

