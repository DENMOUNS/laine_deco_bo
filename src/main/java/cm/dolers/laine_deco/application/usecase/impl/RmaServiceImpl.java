package cm.dolers.laine_deco.application.usecase.impl;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.mapper.RmaMapper;
import cm.dolers.laine_deco.application.usecase.RmaService;
import cm.dolers.laine_deco.domain.exception.ErrorCode;
import cm.dolers.laine_deco.domain.exception.ValidationException;
import cm.dolers.laine_deco.infrastructure.persistence.entity.RmaEntity;
import cm.dolers.laine_deco.infrastructure.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RmaServiceImpl implements RmaService {
    private final RmaJpaRepository rmaRepository;
    private final OrderJpaRepository orderRepository;
    private final RmaMapper rmaMapper;

    @Override
    @Transactional
    public RmaResponse createRma(CreateRmaRequest request) {
        log.info("Creating RMA for order: {}", request.orderId());

        var order = orderRepository.findById(request.orderId())
            .orElseThrow(() -> new ValidationException(ErrorCode.ORDER_NOT_FOUND, "ID: " + request.orderId()));

        try {
            var rma = new RmaEntity();
            rma.setOrder(order);
            rma.setRmaNumber("RMA-" + System.currentTimeMillis());
            rma.setReason(request.reason());
            rma.setStatus("PENDING");

            var saved = rmaRepository.save(rma);
            log.info("RMA created: {}", saved.getId());
            return rmaMapper.toResponse(saved);
        } catch (Exception ex) {
            log.error("Error creating RMA", ex);
            throw new ValidationException(ErrorCode.OPERATION_FAILED, "Error creating RMA", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public RmaResponse getRmaById(Long rmaId) {
        var rma = rmaRepository.findById(rmaId)
            .orElseThrow(() -> new ValidationException(ErrorCode.RMA_NOT_FOUND, "ID: " + rmaId));
        return rmaMapper.toResponse(rma);
    }

    @Override
    @Transactional(readOnly = true)
    public RmaResponse getRmaByNumber(String rmaNumber) {
        var rma = rmaRepository.findByRmaNumber(rmaNumber)
            .orElseThrow(() -> new ValidationException(ErrorCode.RMA_NOT_FOUND, "Number: " + rmaNumber));
        return rmaMapper.toResponse(rma);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RmaResponse> getAllRmas(Pageable pageable) {
        return rmaRepository.findAll(pageable).map(rmaMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RmaResponse> getUserRmas(Long userId, Pageable pageable) {
        // TODO: Implémenter avec jointure User -> Order -> RMA
        return rmaRepository.findAll(pageable).map(rmaMapper::toResponse);
    }

    @Override
    @Transactional
    public RmaResponse updateRmaStatus(Long rmaId, String status) {
        var rma = rmaRepository.findById(rmaId)
            .orElseThrow(() -> new ValidationException(ErrorCode.RMA_NOT_FOUND, "ID: " + rmaId));
        rma.setStatus(status);
        var updated = rmaRepository.save(rma);
        log.info("RMA status updated: {} -> {}", rmaId, status);
        return rmaMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void approveRma(Long rmaId) {
        var rma = rmaRepository.findById(rmaId)
            .orElseThrow(() -> new ValidationException(ErrorCode.RMA_NOT_FOUND, "ID: " + rmaId));
        rma.setStatus("APPROVED");
        rma.setTrackingNumber("TRACK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        rmaRepository.save(rma);
        log.info("RMA approved: {}", rmaId);
    }

    @Override
    @Transactional
    public void rejectRma(Long rmaId) {
        var rma = rmaRepository.findById(rmaId)
            .orElseThrow(() -> new ValidationException(ErrorCode.RMA_NOT_FOUND, "ID: " + rmaId));
        rma.setStatus("REJECTED");
        rmaRepository.save(rma);
        log.info("RMA rejected: {}", rmaId);
    }

    @Override
    @Transactional
    public void resolveRma(Long rmaId) {
        var rma = rmaRepository.findById(rmaId)
            .orElseThrow(() -> new ValidationException(ErrorCode.RMA_NOT_FOUND, "ID: " + rmaId));
        rma.setStatus("RESOLVED");
        rma.setResolvedAt(Instant.now());
        rmaRepository.save(rma);
        log.info("RMA resolved: {}", rmaId);
    }
}
