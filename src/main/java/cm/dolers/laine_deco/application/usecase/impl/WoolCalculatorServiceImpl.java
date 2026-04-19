package cm.dolers.laine_deco.application.usecase.impl;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.mapper.WoolCalculatorMapper;
import cm.dolers.laine_deco.application.usecase.WoolCalculatorService;
import cm.dolers.laine_deco.domain.exception.ErrorCode;
import cm.dolers.laine_deco.domain.exception.UserException;
import cm.dolers.laine_deco.infrastructure.persistence.entity.*;
import cm.dolers.laine_deco.infrastructure.persistence.repository.*;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor

public class WoolCalculatorServiceImpl implements WoolCalculatorService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(WoolCalculatorServiceImpl.class);
    private final WoolCalculationJpaRepository calculatorRepository;
    private final UserJpaRepository userRepository;
    private final WoolCalculatorMapper calculatorMapper;

    @Override
    @Transactional(readOnly = true)
    public WoolCalculatorResponse calculateWool(CreateWoolCalculatorRequest request) {
        log.info("Calculating wool for project: {}", request.projectName());

        // Formule de calcul approximatif
        int area = request.width() * request.height(); // cm²
        int yardagePerCm2 = getYardagePerCm2(request.yarnWeight());
        int estimatedYardage = (area * yardagePerCm2) / 100; // Approximation

        BigDecimal weightPerYard = getWeightPerYard(request.yarnWeight());
        BigDecimal estimatedWeight = weightPerYard.multiply(new BigDecimal(estimatedYardage))
                .divide(new BigDecimal(1760), 2, RoundingMode.HALF_UP); // Convert yards to weight

        int numberOfSkeins = (int) Math.ceil((double) estimatedYardage / 400); // Assume 400 yards per skein

        var response = new WoolCalculatorResponse(
                null,
                request.projectName(),
                request.yarnWeight(),
                String.valueOf(request.needleSize()),
                Double.valueOf(request.width()),
                Double.valueOf(request.height()),
                Double.valueOf(estimatedYardage),
                estimatedWeight.doubleValue(),
                numberOfSkeins,
                null);
        return response;
    }

    @Override
    @Transactional
    public WoolCalculatorResponse saveCalculation(CreateWoolCalculatorRequest request) {
        log.info("Saving wool calculation: {}", request.projectName());
         Long userId = getCurrentUserId();

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, "ID: " + userId));

        try {
            var calculation = calculateWool(request);
            var entity = new WoolCalculationEntity();
            entity.setUser(user);
            entity.setProjectName(request.projectName());
            entity.setYarnWeight(request.yarnWeight());
            entity.setNeedleSize(String.valueOf(request.needleSize()));
            entity.setWidth(Double.valueOf(request.width()));
            entity.setHeight(Double.valueOf(request.height()));
            entity.setEstimatedYardage(calculation.estimatedYardage());
            entity.setEstimatedWeight(calculation.estimatedWeight());
            entity.setNumberOfSkeins(calculation.numberOfSkeins());

            var saved = calculatorRepository.save(entity);
            log.info("Calculation saved: {}", saved.getId());
            return calculatorMapper.toResponse(saved);
        } catch (Exception ex) {
            log.error("Error saving calculation", ex);
            throw new UserException(ErrorCode.OPERATION_FAILED, "Error saving calculation", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public WoolCalculatorResponse getCalculationById(Long calculationId) {
        var calculation = calculatorRepository.findById(calculationId)
                .orElseThrow(() -> new UserException(ErrorCode.CALC_NOT_FOUND, "ID: " + calculationId));
        return calculatorMapper.toResponse(calculation);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WoolCalculatorResponse> getUserCalculations(Long userId, Pageable pageable) {
        return calculatorRepository.findByUserId(userId, pageable).map(calculatorMapper::toResponse);
    }

    @Override
    @Transactional
    public void deleteCalculation(Long calculationId) {
        if (!calculatorRepository.existsById(calculationId)) {
            throw new UserException(ErrorCode.CALC_NOT_FOUND, "ID: " + calculationId);
        }
        calculatorRepository.deleteById(calculationId);
        log.info("Calculation deleted: {}", calculationId);
    }

    private int getYardagePerCm2(String yarnWeight) {
        return switch (yarnWeight.toUpperCase()) {
            case "LACE" -> 8;
            case "FINGERING" -> 6;
            case "SPORT" -> 5;
            case "WORSTED" -> 3;
            case "BULKY" -> 2;
            case "SUPER_BULKY" -> 1;
            default -> 4;
        };
    }

    private BigDecimal getWeightPerYard(String yarnWeight) {
        return switch (yarnWeight.toUpperCase()) {
            case "LACE" -> new BigDecimal("0.5");
            case "FINGERING" -> new BigDecimal("1.0");
            case "SPORT" -> new BigDecimal("1.5");
            case "WORSTED" -> new BigDecimal("2.0");
            case "BULKY" -> new BigDecimal("3.0");
            case "SUPER_BULKY" -> new BigDecimal("4.0");
            default -> new BigDecimal("1.8");
        };
    }

    private Long getCurrentUserId() {
        var authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof cm.dolers.laine_deco.infrastructure.security.AuthenticatedUser user) {
            return user.getId();
        }
        throw new UserException(ErrorCode.AUTH_UNAUTHORIZED, "Utilisateur non authentifié");
    }
}
