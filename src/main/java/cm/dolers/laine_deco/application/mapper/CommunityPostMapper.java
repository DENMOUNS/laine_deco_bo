package cm.dolers.laine_deco.application.mapper;

import cm.dolers.laine_deco.application.dto.CommunityCommentResponse;
import cm.dolers.laine_deco.application.dto.CommunityPostResponse;
import cm.dolers.laine_deco.infrastructure.persistence.entity.CommunityCommentEntity;
import cm.dolers.laine_deco.infrastructure.persistence.entity.CommunityPostEntity;

public interface CommunityPostMapper {
    CommunityPostResponse toResponse(CommunityPostEntity post);
    CommunityCommentResponse toCommentResponse(CommunityCommentEntity comment);
}

