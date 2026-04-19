package cm.dolers.laine_deco.application.mapper.impl;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.mapper.LoyaltyMapper;
import cm.dolers.laine_deco.infrastructure.persistence.entity.*;
import org.springframework.stereotype.Component;

@Component
public class LoyaltyMapperImpl implements LoyaltyMapper {
    @Override
    public UserLoyaltyResponse toResponse(UserLoyaltyProfile profile) {
        return new UserLoyaltyResponse(
            profile.getUser() != null ? profile.getUser().getId() : null,
            profile.getTotalPoints(),
            profile.getAvailablePoints(),
            profile.getCurrentTier() != null ? profile.getCurrentTier().name() : null,
            null, // tierDescription
            profile.getTierReachedAt(),
            profile.getTotalSpent(),
            profile.getOrderCount(),
            profile.getCreatedAt()
        );
    }

    @Override
    public UserBadgeResponse toResponse(UserBadgeEntity badge) {
        return new UserBadgeResponse(
            badge.getId(),
            badge.getUser() != null ? badge.getUser().getId() : null,
            badge.getBadge() != null ? badge.getBadge().getId() : null,
            badge.getBadge() != null ? badge.getBadge().getName() : null,
            badge.getEarnedAt()
        );
    }

    @Override
    public LoyaltyRedemptionResponse toResponse(LoyaltyRedemption redemption) {
        return new LoyaltyRedemptionResponse(
            redemption.getId(),
            redemption.getRewardType() != null ? redemption.getRewardType().name() : null,
            null, // rewardLabel
            redemption.getPointsUsed(),
            redemption.getRewardValue(),
            redemption.getStatus() != null ? redemption.getStatus().name() : null,
            redemption.getAppliedAt(),
            redemption.getExpiresAt(),
            redemption.getNotes(),
            redemption.getCreatedAt()
        );
    }
}
