package cm.dolers.laine_deco.application.mapper;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.infrastructure.persistence.entity.NotificationEntity;

/**
 * Interface Mapper pour Notification
 */
public interface NotificationMapper {
    NotificationResponse toResponse(NotificationEntity notification);
}
