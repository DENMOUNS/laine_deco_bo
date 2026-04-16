package cm.dolers.laine_deco.interfaces.rest.controller.admin;

import cm.dolers.laine_deco.application.dto.AuditLogResponse;
import cm.dolers.laine_deco.application.usecase.AuditService;
import cm.dolers.laine_deco.infrastructure.config.PaginationConstants;
import cm.dolers.laine_deco.infrastructure.config.RequestInfoExtractor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

/**
 * Admin Controller pour consulter les logs d'audit
 */
@RestController
@RequestMapping("/api/admin/audit-logs")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminAuditLogController {
    private final AuditService auditService;

    /**
     * Récupère les logs d'audit d'un utilisateur
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<AuditLogResponse>> getUserAuditLogs(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        
        int normalizedSize = PaginationConstants.normalizePageSize(pageSize);
        Pageable pageable = PageRequest.of(page, normalizedSize);
        
        log.info("GET /api/admin/audit-logs/user/{} - page: {}, size: {}", userId, page, normalizedSize);
        
        var results = auditService.getUserAuditLogs(userId, pageable);
        return ResponseEntity.ok(results);
    }

    /**
     * Récupère les logs par action
     */
    @GetMapping("/action/{action}")
    public ResponseEntity<Page<AuditLogResponse>> getByAction(
            @PathVariable String action,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        
        int normalizedSize = PaginationConstants.normalizePageSize(pageSize);
        Pageable pageable = PageRequest.of(page, normalizedSize);
        
        log.info("GET /api/admin/audit-logs/action/{} - page: {}, size: {}", action, page, normalizedSize);
        
        var results = auditService.getAuditLogsByAction(action, pageable);
        return ResponseEntity.ok(results);
    }

    /**
     * Récupère les logs pour une entité spécifique
     */
    @GetMapping("/entity/{entityType}/{entityId}")
    public ResponseEntity<Page<AuditLogResponse>> getByEntity(
            @PathVariable String entityType,
            @PathVariable Long entityId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        
        int normalizedSize = PaginationConstants.normalizePageSize(pageSize);
        Pageable pageable = PageRequest.of(page, normalizedSize);
        
        log.info("GET /api/admin/audit-logs/entity/{}/{} - page: {}, size: {}", 
            entityType, entityId, page, normalizedSize);
        
        var results = auditService.getAuditLogsForEntity(entityType, entityId, pageable);
        return ResponseEntity.ok(results);
    }

    /**
     * Recherche avancée dans les logs d'audit
     */
    @GetMapping("/search")
    public ResponseEntity<Page<AuditLogResponse>> search(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String entityType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int pageSize) {
        
        int normalizedSize = PaginationConstants.normalizePageSize(pageSize);
        Pageable pageable = PageRequest.of(page, normalizedSize);
        
        log.info("GET /api/admin/audit-logs/search - userId: {}, action: {}, status: {}", 
            userId, action, status);
        
        var results = auditService.searchAuditLogs(userId, action, entityType, status, startTime, endTime, pageable);
        return ResponseEntity.ok(results);
    }

    /**
     * Récupère les logs échoués
     */
    @GetMapping("/failed")
    public ResponseEntity<Page<AuditLogResponse>> getFailedLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        
        int normalizedSize = PaginationConstants.normalizePageSize(pageSize);
        Pageable pageable = PageRequest.of(page, normalizedSize);
        
        log.info("GET /api/admin/audit-logs/failed - page: {}, size: {}", page, normalizedSize);
        
        var results = auditService.getFailedAuditLogs(pageable);
        return ResponseEntity.ok(results);
    }

    /**
     * Exporte les logs d'audit en CSV
     */
    @GetMapping("/export")
    public ResponseEntity<String> exportToCSV(
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String entityType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endTime) {
        
        log.info("GET /api/admin/audit-logs/export - action: {}, entityType: {}", action, entityType);
        
        String csv = auditService.exportAuditLogsToCSV(action, entityType, startTime, endTime);
        
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=audit-logs.csv")
            .header("Content-Type", "text/csv")
            .body(csv);
    }

    /**
     * Supprime les logs expirés
     */
    @DeleteMapping("/cleanup/{daysOld}")
    public ResponseEntity<String> cleanupOldLogs(
            @PathVariable int daysOld,
            HttpServletRequest request) {
        
        log.warn("DELETE /api/admin/audit-logs/cleanup/{} - IP: {}", daysOld, 
            RequestInfoExtractor.getClientIpAddress(request));
        
        long deleted = auditService.deleteOldAuditLogs(daysOld);
        
        return ResponseEntity.ok(String.format("Deleted audit logs older than %d days", daysOld));
    }

    /**
     * Récupère des statistiques sur les actions d'un utilisateur
     */
    @GetMapping("/stats/user/{userId}")
    public ResponseEntity<String> getUserActionStats(@PathVariable Long userId) {
        log.info("GET /api/admin/audit-logs/stats/user/{}", userId);
        
        long createCount = auditService.countUserActions(userId, "CREATE");
        long updateCount = auditService.countUserActions(userId, "UPDATE");
        long deleteCount = auditService.countUserActions(userId, "DELETE");
        long loginCount = auditService.countUserActions(userId, "LOGIN");
        
        String stats = String.format(
            "User %d - CREATE: %d, UPDATE: %d, DELETE: %d, LOGIN: %d",
            userId, createCount, updateCount, deleteCount, loginCount
        );
        
        return ResponseEntity.ok(stats);
    }
}
