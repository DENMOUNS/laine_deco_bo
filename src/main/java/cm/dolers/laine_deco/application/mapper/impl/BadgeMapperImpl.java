package cm.dolers.laine_deco.application.mapper.impl;


import cm.dolers.laine_deco.application.dto.BadgeResponse;
import cm.dolers.laine_deco.application.dto.UserBadgeResponse;
import cm.dolers.laine_deco.application.mapper.BadgeMapper;
import cm.dolers.laine_deco.infrastructure.persistence.entity.BadgeEntity;
import cm.dolers.laine_deco.infrastructure.persistence.entity.UserBadgeEntity;
import org.springframework.stereotype.Component;

@Component
public class BadgeMapperImpl implements BadgeMapper {
    @Override
    public BadgeResponse toResponse(BadgeEntity badge) {
        return new BadgeResponse(badge.getId(), badge.getName(), badge.getDescription(), 
            badge.getIcon(), badge.getCriteria(), badge.getCreatedAt());
    }

    @Override
    public UserBadgeResponse toUserBadgeResponse(UserBadgeEntity UserBadgeEntity) {
        return new UserBadgeResponse(UserBadgeEntity.getId(), UserBadgeEntity.getUser().getId(), 
            UserBadgeEntity.getBadge().getId(), UserBadgeEntity.getBadge().getName(), UserBadgeEntity.getEarnedAt());
    }
}


