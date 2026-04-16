package cm.dolers.laine_deco.application.mapper.impl;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.mapper.NotificationMapper;
import cm.dolers.laine_deco.infrastructure.persistence.entity.NotificationEntity;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapperImpl implements NotificationMapper {

    @Override
    public NotificationResponse toResponse(NotificationEntity notification) {
        return new NotificationResponse(
            notification.getId(),
            notification.getUser().getId(),
            notification.getType(),
            notification.getTitle(),
            notification.getMessage(),
            notification.getRelatedId(),
            notification.getIsRead(),
            notification.getCreatedAt()
        );
    }
}
