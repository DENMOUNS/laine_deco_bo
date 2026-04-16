package cm.dolers.laine_deco.application.mapper;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.infrastructure.persistence.entity.UserLoyaltyProfile;
import cm.dolers.laine_deco.infrastructure.persistence.entity.UserBadge;
import cm.dolers.laine_deco.infrastructure.persistence.entity.LoyaltyRedemption;

/**
 * Mappers pour loyauté
 */
public interface LoyaltyMapper {
    UserLoyaltyResponse toResponse(UserLoyaltyProfile profile);
    
    UserBadgeResponse toResponse(UserBadge badge);
    
    LoyaltyRedemptionResponse toResponse(LoyaltyRedemption redemption);
}
