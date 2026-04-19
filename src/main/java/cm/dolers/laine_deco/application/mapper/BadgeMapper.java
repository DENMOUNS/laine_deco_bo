package cm.dolers.laine_deco.application.mapper;

import cm.dolers.laine_deco.application.dto.BadgeResponse;
import cm.dolers.laine_deco.application.dto.UserBadgeResponse;
import cm.dolers.laine_deco.infrastructure.persistence.entity.BadgeEntity;
import cm.dolers.laine_deco.infrastructure.persistence.entity.UserBadgeEntity;

public interface BadgeMapper {
    BadgeResponse toResponse(BadgeEntity badge);
    UserBadgeResponse toUserBadgeResponse(UserBadgeEntity UserBadgeEntity);
}


