package cm.dolers.laine_deco.application.mapper;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.infrastructure.persistence.entity.LoyaltyRedemption;
import cm.dolers.laine_deco.infrastructure.persistence.entity.UserBadgeEntity;
import cm.dolers.laine_deco.infrastructure.persistence.entity.UserLoyaltyProfile;

/**
 * Mappers pour loyautÃ©
 */
public interface LoyaltyMapper {
    UserLoyaltyResponse toResponse(UserLoyaltyProfile profile);
    
    UserBadgeResponse toResponse(UserBadgeEntity badge);
    
    LoyaltyRedemptionResponse toResponse(LoyaltyRedemption redemption);
}

