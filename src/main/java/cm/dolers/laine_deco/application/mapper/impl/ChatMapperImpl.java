package cm.dolers.laine_deco.application.mapper.impl;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.mapper.ChatMapper;
import cm.dolers.laine_deco.infrastructure.persistence.entity.ConversationEntity;
import cm.dolers.laine_deco.infrastructure.persistence.entity.ChatMessageEntity;
import org.springframework.stereotype.Component;

@Component
public class ChatMapperImpl implements ChatMapper {

    @Override
    public ChatConversationResponse toConversationResponse(ConversationEntity entity) {
        return new ChatConversationResponse(
            entity.getId(),

            entity.getClient() != null ? entity.getClient().getId() : null,

            entity.getAgent() != null ? entity.getAgent().getId() : null,

            entity.getStatus(),
            entity.getCreatedAt(),
            entity.getClosedAt()
        );
    }
    @Override
    public ChatMessageResponse toMessageResponse(ChatMessageEntity entity) {
        return new ChatMessageResponse(
            entity.getId(),

            entity.getConversation() != null ? entity.getConversation().getId() : null,

            entity.getSender() != null ? entity.getSender().getId() : null,
            entity.getSender() != null ? entity.getSender().getName() : null,

            entity.getMessage(),
            entity.getIsRead(),
            entity.getCreatedAt()
        );
    }
}