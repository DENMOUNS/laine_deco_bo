package cm.dolers.laine_deco.application.mapper;

import cm.dolers.laine_deco.application.dto.BlogPostResponse;
import cm.dolers.laine_deco.infrastructure.persistence.entity.BlogPostEntity;

public interface BlogPostMapper {
    BlogPostResponse toResponse(BlogPostEntity post);
}

