package cm.dolers.laine_deco.application.mapper;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.infrastructure.persistence.entity.*;

/**
 * Mapper pour Chat
 */
public interface ChatMapper {
    ChatConversationResponse toConversationResponse(ChatConversationEntity entity);
    ChatMessageResponse toMessageResponse(ChatMessageEntity entity);
}

/**
 * Mapper pour KnittingProject
 */
public interface KnittingProjectMapper {
    KnittingProjectResponse toResponse(KnittingProjectEntity entity);
}

/**
 * Mapper pour Payment
 */
public interface PaymentMapper {
    PaymentResponse toResponse(PaymentEntity entity);
}
