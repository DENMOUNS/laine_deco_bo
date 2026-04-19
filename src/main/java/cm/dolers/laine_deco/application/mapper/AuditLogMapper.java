package cm.dolers.laine_deco.application.mapper;

import cm.dolers.laine_deco.application.dto.AuditLogResponse;
import cm.dolers.laine_deco.infrastructure.persistence.entity.AuditLogEntity;
import org.springframework.stereotype.Component;

@Component
public class AuditLogMapper {

    public AuditLogResponse toResponse(AuditLogEntity entity) {
        if (entity == null) {
            return null;
        }

        return new AuditLogResponse(
            entity.getId(),
            entity.getUserId(),
            entity.getUserEmail(),
            entity.getAction(),
            entity.getEntityType(),
            entity.getEntityId(),
            entity.getDescription(),
            entity.getIpAddress(),
            entity.getUserAgent(),
            entity.getHttpMethod(),
            entity.getRequestPath(),
            entity.getQueryString(),
            entity.getStatus(),
            entity.getErrorMessage(),
            entity.getOldData(),
            entity.getNewData(),
            entity.getTimestamp()
        );
    }

    public AuditLogEntity toEntity(AuditLogResponse response) {
        if (response == null) {
            return null;
        }

        AuditLogEntity entity = new AuditLogEntity();
        entity.setId(response.id());
        entity.setUserId(response.userId());
        entity.setUserEmail(response.userEmail());
        entity.setAction(response.action());
        entity.setEntityType(response.entityType());
        entity.setEntityId(response.entityId());
        entity.setDescription(response.description());
        entity.setIpAddress(response.ipAddress());
        entity.setUserAgent(response.userAgent());
        entity.setHttpMethod(response.httpMethod());
        entity.setRequestPath(response.requestPath());
        entity.setQueryString(response.queryString());
        entity.setStatus(response.status());
        entity.setErrorMessage(response.errorMessage());
        entity.setOldData(response.oldData());
        entity.setNewData(response.newData());
        entity.setTimestamp(response.timestamp());

        return entity;
    }
}

