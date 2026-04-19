package cm.dolers.laine_deco.application.usecase.impl;

import cm.dolers.laine_deco.application.dto.AuditLogResponse;
import cm.dolers.laine_deco.application.dto.CreateAuditLogRequest;
import cm.dolers.laine_deco.application.mapper.AuditLogMapper;
import cm.dolers.laine_deco.application.usecase.AuditService;
import cm.dolers.laine_deco.infrastructure.persistence.entity.AuditLogEntity;
import cm.dolers.laine_deco.infrastructure.persistence.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor

@Transactional
public class AuditServiceImpl implements AuditService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AuditServiceImpl.class);
    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    @Override
    public AuditLogResponse logAction(
            Long userId,
            String userEmail,
            String action,
            String entityType,
            Long entityId,
            String description,
            String httpMethod,
            String requestPath,
            String queryString,
            String ipAddress,
            String userAgent) {

        return logActionWithData(
                userId, userEmail, action, entityType, entityId, description,
                null, null, httpMethod, requestPath, queryString, ipAddress, userAgent, "SUCCESS", null);
    }

    @Override
    public AuditLogResponse logActionWithData(
            Long userId,
            String userEmail,
            String action,
            String entityType,
            Long entityId,
            String description,
            String oldData,
            String newData,
            String httpMethod,
            String requestPath,
            String queryString,
            String ipAddress,
            String userAgent,
            String status,
            String errorMessage) {

        AuditLogEntity auditLog = new AuditLogEntity();
        auditLog.setUserId(userId);
        auditLog.setUserEmail(userEmail);
        auditLog.setAction(action);
        auditLog.setEntityType(entityType);
        auditLog.setEntityId(entityId);
        auditLog.setDescription(description);
        auditLog.setOldData(oldData);
        auditLog.setNewData(newData);
        auditLog.setHttpMethod(httpMethod);
        auditLog.setRequestPath(requestPath);
        auditLog.setQueryString(queryString);
        auditLog.setIpAddress(ipAddress);
        auditLog.setUserAgent(userAgent);
        auditLog.setStatus(status != null ? status : "SUCCESS");
        auditLog.setErrorMessage(errorMessage);
        auditLog.setTimestamp(Instant.now());

        AuditLogEntity saved = auditLogRepository.save(auditLog);

        log.info("Audit logged - User: {}, Action: {} {} {}, Entity: {}/{}",
                userEmail, httpMethod, requestPath, action, entityType, entityId);

        return auditLogMapper.toResponse(saved);
    }

    @Override
    public AuditLogResponse logActionFromRequest(
            CreateAuditLogRequest request,
            Long userId,
            String userEmail,
            String ipAddress,
            String userAgent) {

        return logActionWithData(
                userId, userEmail,
                request.action(),
                request.entityType(),
                request.entityId(),
                request.description(),
                request.oldData(),
                request.newData(),
                request.httpMethod(),
                request.requestPath(),
                request.queryString(),
                ipAddress, userAgent,
                request.status(),
                request.errorMessage());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogResponse> getUserAuditLogs(Long userId, Pageable pageable) {
        return auditLogRepository.findByUserId(userId, pageable)
                .map(auditLogMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogResponse> getAuditLogsByAction(String action, Pageable pageable) {
        return auditLogRepository.findByAction(action, pageable)
                .map(auditLogMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogResponse> getAuditLogsForEntity(String entityType, Long entityId, Pageable pageable) {
        return auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId, pageable)
                .map(auditLogMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogResponse> searchAuditLogs(
            Long userId,
            String action,
            String entityType,
            String status,
            Instant startTime,
            Instant endTime,
            Pageable pageable) {

        Instant start = startTime != null ? startTime : Instant.now().minus(30, ChronoUnit.DAYS);
        Instant end = endTime != null ? endTime : Instant.now();

        return auditLogRepository.searchAuditLogs(userId, action, entityType, status, start, end, pageable)
                .map(auditLogMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogResponse> getFailedAuditLogs(Pageable pageable) {
        return auditLogRepository.findFailedAuditLogs(pageable)
                .map(auditLogMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public String exportAuditLogsToCSV(String action, String entityType, Instant startTime, Instant endTime) {
        Instant start = startTime != null ? startTime : Instant.now().minus(30, ChronoUnit.DAYS);
        Instant end = endTime != null ? endTime : Instant.now();

        Page<AuditLogEntity> logs = auditLogRepository.findAuditLogsByDateRange(
                start, end,
                org.springframework.data.domain.PageRequest.of(0, 10000) // Récupérer max 10k logs
        );

        StringBuilder csv = new StringBuilder();
        csv.append("ID,User ID,Email,Action,Entity Type,Entity ID,Description,IP,Status,Timestamp\n");

        logs.forEach(log -> csv.append(String.format(
                "%d,%d,%s,%s,%s,%d,%s,%s,%s,%s\n",
                log.getId(),
                log.getUserId() != null ? log.getUserId() : "",
                escapeCSV(log.getUserEmail()),
                log.getAction(),
                log.getEntityType(),
                log.getEntityId() != null ? log.getEntityId() : "",
                escapeCSV(log.getDescription()),
                log.getIpAddress(),
                log.getStatus(),
                log.getTimestamp())));

        log.info("Exported {} audit logs to CSV", logs.getTotalElements());
        return csv.toString();
    }

    @Override
    public long deleteOldAuditLogs(int daysOld) {
        Instant cutoff = Instant.now().minus(daysOld, ChronoUnit.DAYS);
        auditLogRepository.deleteOldAuditLogs(cutoff);
        log.info("Deleted audit logs older than {} days", daysOld);
        return 0; // Hibernate ne retourne pas le nombre de lignes supprimées directement
    }

    @Override
    @Transactional(readOnly = true)
    public long countUserActions(Long userId, String action) {
        return auditLogRepository.countByUserIdAndAction(userId, action);
    }

    /**
     * Échappe les caractères spéciaux pour CSV
     */
    private String escapeCSV(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
