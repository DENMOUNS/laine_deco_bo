package cm.dolers.laine_deco.application.dto;

import java.time.Instant;

/**
 * DTO pour réponse d'audit log
 */
public record AuditLogResponse(
    Long id,
    Long userId,
    String userEmail,
    String action,
    String entityType,
    Long entityId,
    String description,
    String ipAddress,
    String userAgent,
    String httpMethod,
    String requestPath,
    String queryString,
    String status,
    String errorMessage,
    String oldData,
    String newData,
    Instant timestamp
) {}

