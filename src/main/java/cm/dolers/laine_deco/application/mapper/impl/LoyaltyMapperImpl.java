package cm.dolers.laine_deco.application.mapper.impl;

import cm.dolers.laine_deco.application.dto.UserLoyaltyResponse;
import cm.dolers.laine_deco.application.dto.UserBadgeResponse;
import cm.dolers.laine_deco.application.dto.LoyaltyRedemptionResponse;
import cm.dolers.laine_deco.application.mapper.LoyaltyMapper;
import cm.dolers.laine_deco.infrastructure.persistence.entity.UserLoyaltyProfile;
import cm.dolers.laine_deco.infrastructure.persistence.entity.UserBadge;
import cm.dolers.laine_deco.infrastructure.persistence.entity.LoyaltyRedemption;
import org.springframework.stereotype.Component;

@Component
public class LoyaltyMapperImpl implements LoyaltyMapper {

    @Override
    public UserLoyaltyResponse toResponse(UserLoyaltyProfile profile) {
        return new UserLoyaltyResponse(
            profile.getUser().getId(),
            profile.getTotalPoints(),
            profile.getAvailablePoints(),
            profile.getCurrentTier() != null ? profile.getCurrentTier().toString() : "STANDARD",
            profile.getCurrentTier() != null ? profile.getCurrentTier().getDescription() : "",
            profile.getTierReachedAt(),
            profile.getTotalSpent(),
            profile.getOrderCount(),
            profile.getCreatedAt()
        );
    }

    @Override
    public UserBadgeResponse toResponse(UserBadge badge) {
        return new UserBadgeResponse(
            badge.getId(),
            badge.getBadgeType() != null ? badge.getBadgeType().toString() : null,
            badge.getBadgeType() != null ? badge.getBadgeType().getLabel() : null,
            badge.getBadgeType() != null ? badge.getBadgeType().getIcon() : null,
            badge.getBadgeType() != null ? badge.getBadgeType().getDescription() : null,
            badge.getEarnedAt()
        );
    }

    @Override
    public LoyaltyRedemptionResponse toResponse(LoyaltyRedemption redemption) {
        return new LoyaltyRedemptionResponse(
            redemption.getId(),
            redemption.getRewardType() != null ? redemption.getRewardType().toString() : null,
            redemption.getRewardType() != null ? redemption.getRewardType().getLabel() : null,
            redemption.getPointsUsed(),
            redemption.getRewardValue(),
            redemption.getStatus() != null ? redemption.getStatus().toString() : null,
            redemption.getAppliedAt(),
            redemption.getExpiresAt(),
            redemption.getNotes(),
            redemption.getCreatedAt()
        );
    }
}
