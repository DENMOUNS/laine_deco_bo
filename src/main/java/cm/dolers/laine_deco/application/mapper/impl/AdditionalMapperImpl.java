package cm.dolers.laine_deco.application.mapper.impl;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.mapper.*;
import cm.dolers.laine_deco.infrastructure.persistence.entity.*;
import org.springframework.stereotype.Component;

@Component
class BadgeMapperImpl implements BadgeMapper {
    @Override
    public BadgeResponse toResponse(BadgeEntity badge) {
        return new BadgeResponse(badge.getId(), badge.getName(), badge.getDescription(), 
            badge.getIcon(), badge.getCriteria(), badge.getCreatedAt());
    }

    @Override
    public UserBadgeResponse toUserBadgeResponse(UserBadgeEntity userBadge) {
        return new UserBadgeResponse(userBadge.getId(), userBadge.getUser().getId(), 
            userBadge.getBadge().getId(), userBadge.getBadge().getName(), userBadge.getEarnedAt());
    }
}

@Component
class CommunityPostMapperImpl implements CommunityPostMapper {
    @Override
    public CommunityPostResponse toResponse(CommunityPostEntity post) {
        return new CommunityPostResponse(post.getId(), post.getUser().getId(), post.getUser().getName(),
            post.getTitle(), post.getContent(), post.getLikesCount(), post.getCommentsCount(),
            post.getViewsCount(), post.getCreatedAt());
    }

    @Override
    public CommunityCommentResponse toCommentResponse(CommunityCommentEntity comment) {
        return new CommunityCommentResponse(comment.getId(), comment.getPost().getId(),
            comment.getUser().getId(), comment.getUser().getName(), comment.getComment(),
            comment.getLikesCount(), comment.getCreatedAt());
    }
}

@Component
class InvoiceMapperImpl implements InvoiceMapper {
    @Override
    public InvoiceResponse toResponse(InvoiceEntity invoice) {
        return new InvoiceResponse(invoice.getId(), invoice.getOrder().getId(),
            invoice.getInvoiceNumber(), invoice.getSubtotal(), invoice.getTaxAmount(),
            invoice.getTotalAmount(), invoice.getStatus(), invoice.getIssuedAt(), invoice.getDueDate());
    }
}

@Component
class WoolCalculatorMapperImpl implements WoolCalculatorMapper {
    @Override
    public WoolCalculatorResponse toResponse(WoolCalculatorEntity calculator) {
        return new WoolCalculatorResponse(calculator.getId(), calculator.getProjectName(),
            calculator.getYarnWeight(), calculator.getNeedleSize(), calculator.getWidth(),
            calculator.getHeight(), calculator.getEstimatedYardage(), calculator.getEstimatedWeight(),
            calculator.getNumberOfSkeins(), calculator.getCreatedAt());
    }
}

@Component
class KnittingPatternMapperImpl implements KnittingPatternMapper {
    @Override
    public KnittingPatternResponse toResponse(KnittingPatternEntity pattern) {
        return new KnittingPatternResponse(pattern.getId(), pattern.getName(),
            pattern.getAuthor(), pattern.getDescription(), pattern.getSkillLevel(),
            pattern.getEstimatedHours(), pattern.getYarnType(), pattern.getNeedleSize(),
            pattern.getUrl(), pattern.getDownloadCount(), pattern.getCreatedAt());
    }
}

@Component
class PromoEventMapperImpl implements PromoEventMapper {
    @Override
    public PromoEventResponse toResponse(PromoEventEntity event) {
        return new PromoEventResponse(event.getId(), event.getName(), event.getDescription(),
            event.getDiscountPercentage(), event.getDiscountAmount(), event.getStartDate(),
            event.getEndDate(), event.getStatus(), event.getCreatedAt());
    }
}

@Component
class RmaMapperImpl implements RmaMapper {
    @Override
    public RmaResponse toResponse(RmaEntity rma) {
        return new RmaResponse(rma.getId(), rma.getOrder().getId(), rma.getRmaNumber(),
            rma.getReason(), rma.getStatus(), rma.getTrackingNumber(), rma.getCreatedAt(), rma.getResolvedAt());
    }
}

@Component
class SiteConfigMapperImpl implements SiteConfigMapper {
    @Override
    public SiteConfigResponse toResponse(SiteConfigEntity config) {
        return new SiteConfigResponse(config.getId(), config.getKey(), config.getValue(),
            config.getDescription(), config.getUpdatedAt());
    }
}
