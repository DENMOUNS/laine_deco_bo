package cm.dolers.laine_deco.application.dto;

/**
 * DTO pour créer un audit log manuellement
 */
public record CreateAuditLogRequest(
    String action,
    String entityType,
    Long entityId,
    String description,
    String httpMethod,
    String requestPath,
    String queryString,
    String status,
    String errorMessage,
    String oldData,
    String newData
) {}

