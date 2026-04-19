package cm.dolers.laine_deco.application.mapper.impl;

import cm.dolers.laine_deco.application.dto.CommunityCommentResponse;
import cm.dolers.laine_deco.application.dto.CommunityPostResponse;
import cm.dolers.laine_deco.application.mapper.CommunityPostMapper;
import cm.dolers.laine_deco.infrastructure.persistence.entity.CommunityCommentEntity;
import cm.dolers.laine_deco.infrastructure.persistence.entity.CommunityPostEntity;
import org.springframework.stereotype.Component;

@Component
public class CommunityPostMapperImpl implements CommunityPostMapper {
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

