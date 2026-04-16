package cm.dolers.laine_deco.application.mapper;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.infrastructure.persistence.entity.*;

/**
 * Mapper pour Badge
 */
public interface BadgeMapper {
    BadgeResponse toResponse(BadgeEntity badge);
    UserBadgeResponse toUserBadgeResponse(UserBadgeEntity userBadge);
}

/**
 * Mapper pour CommunityPost
 */
public interface CommunityPostMapper {
    CommunityPostResponse toResponse(CommunityPostEntity post);
    CommunityCommentResponse toCommentResponse(CommunityCommentEntity comment);
}

/**
 * Mapper pour Invoice
 */
public interface InvoiceMapper {
    InvoiceResponse toResponse(InvoiceEntity invoice);
}

/**
 * Mapper pour WoolCalculator
 */
public interface WoolCalculatorMapper {
    WoolCalculatorResponse toResponse(WoolCalculatorEntity calculator);
}

/**
 * Mapper pour KnittingPattern
 */
public interface KnittingPatternMapper {
    KnittingPatternResponse toResponse(KnittingPatternEntity pattern);
}

/**
 * Mapper pour PromoEvent
 */
public interface PromoEventMapper {
    PromoEventResponse toResponse(PromoEventEntity event);
}

/**
 * Mapper pour RMA
 */
public interface RmaMapper {
    RmaResponse toResponse(RmaEntity rma);
}

/**
 * Mapper pour SiteConfig
 */
public interface SiteConfigMapper {
    SiteConfigResponse toResponse(SiteConfigEntity config);
}
