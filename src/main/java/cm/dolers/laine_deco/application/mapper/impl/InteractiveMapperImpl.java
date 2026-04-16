package cm.dolers.laine_deco.application.mapper.impl;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.mapper.ChatMapper;
import cm.dolers.laine_deco.application.mapper.KnittingProjectMapper;
import cm.dolers.laine_deco.application.mapper.PaymentMapper;
import cm.dolers.laine_deco.infrastructure.persistence.entity.*;
import org.springframework.stereotype.Component;

@Component
public class ChatMapperImpl implements ChatMapper {

    @Override
    public ChatConversationResponse toConversationResponse(ChatConversationEntity entity) {
        return new ChatConversationResponse(
            entity.getId(),
            entity.getClient().getId(),
            entity.getSupportAgent() != null ? entity.getSupportAgent().getId() : null,
            entity.getStatus(),
            entity.getCreatedAt(),
            entity.getClosedAt()
        );
    }

    @Override
    public ChatMessageResponse toMessageResponse(ChatMessageEntity entity) {
        return new ChatMessageResponse(
            entity.getId(),
            entity.getConversation().getId(),
            entity.getSender().getId(),
            entity.getSender().getName(),
            entity.getMessage(),
            entity.getIsRead(),
            entity.getCreatedAt()
        );
    }
}

@Component
class KnittingProjectMapperImpl implements KnittingProjectMapper {

    @Override
    public KnittingProjectResponse toResponse(KnittingProjectEntity entity) {
        return new KnittingProjectResponse(
            entity.getId(),
            entity.getUser().getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getYarnColor(),
            entity.getYarnType(),
            entity.getNeedleSize(),
            entity.getDifficulty(),
            entity.getEstimatedHours(),
            entity.getStatus(),
            entity.getProgress(),
            entity.getCreatedAt()
        );
    }
}

@Component
class PaymentMapperImpl implements PaymentMapper {

    @Override
    public PaymentResponse toResponse(PaymentEntity entity) {
        return new PaymentResponse(
            entity.getId(),
            entity.getOrder().getId(),
            entity.getAmount(),
            entity.getMethod(),
            entity.getStatus(),
            entity.getTransactionId(),
            entity.getProcessedAt()
        );
    }
}
