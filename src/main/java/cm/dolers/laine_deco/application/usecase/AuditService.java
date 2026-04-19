package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.AuditLogResponse;
import cm.dolers.laine_deco.application.dto.CreateAuditLogRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;

/**
 * Service pour gérer l'audit logging de toutes les actions
 */
public interface AuditService {

    /**
     * Enregistre une action d'audit
     */
    AuditLogResponse logAction(
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
        String userAgent);

    /**
     * Enregistre une action avec ancien et nouveau data (pour les modifications)
     */
    AuditLogResponse logActionWithData(
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
        String errorMessage);

    /**
     * Enregistre une action à partir d'une requête
     */
    AuditLogResponse logActionFromRequest(CreateAuditLogRequest request, Long userId, String userEmail, String ipAddress, String userAgent);

    /**
     * Récupère les logs d'un utilisateur
     */
    Page<AuditLogResponse> getUserAuditLogs(Long userId, Pageable pageable);

    /**
     * Récupère les logs par action
     */
    Page<AuditLogResponse> getAuditLogsByAction(String action, Pageable pageable);

    /**
     * Récupère les logs pour une entité spécifique
     */
    Page<AuditLogResponse> getAuditLogsForEntity(String entityType, Long entityId, Pageable pageable);

    /**
     * Recherche avancée dans les logs
     */
    Page<AuditLogResponse> searchAuditLogs(
        Long userId,
        String action,
        String entityType,
        String status,
        Instant startTime,
        Instant endTime,
        Pageable pageable);

    /**
     * Récupère les logs échoués
     */
    Page<AuditLogResponse> getFailedAuditLogs(Pageable pageable);

    /**
     * Exporte les logs (pour téléchargement CSV ou autres)
     */
    String exportAuditLogsToCSV(String action, String entityType, Instant startTime, Instant endTime);

    /**
     * Supprime les logs expirés
     */
    long deleteOldAuditLogs(int daysOld);

    /**
     * Compte les actions pour statistiques
     */
    long countUserActions(Long userId, String action);
}

