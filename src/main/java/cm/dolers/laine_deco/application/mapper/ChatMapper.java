package cm.dolers.laine_deco.application.mapper;

import cm.dolers.laine_deco.application.dto.ChatConversationResponse;
import cm.dolers.laine_deco.application.dto.ChatMessageResponse;
import cm.dolers.laine_deco.infrastructure.persistence.entity.ConversationEntity;
import cm.dolers.laine_deco.infrastructure.persistence.entity.ChatMessageEntity;

public interface ChatMapper {
    ChatConversationResponse toConversationResponse(ConversationEntity entity);
    ChatMessageResponse toMessageResponse(ChatMessageEntity entity);
}

