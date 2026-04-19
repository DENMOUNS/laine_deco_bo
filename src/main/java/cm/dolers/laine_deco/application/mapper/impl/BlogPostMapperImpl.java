package cm.dolers.laine_deco.application.mapper.impl;


import cm.dolers.laine_deco.application.dto.BlogPostResponse;
import cm.dolers.laine_deco.application.mapper.BlogPostMapper;
import cm.dolers.laine_deco.infrastructure.persistence.entity.BlogPostEntity;
import org.springframework.stereotype.Component;

@Component
public class BlogPostMapperImpl implements BlogPostMapper {

    @Override
    public BlogPostResponse toResponse(BlogPostEntity post) {
        return new BlogPostResponse(
            post.getId(),
            post.getTitle(),
            post.getSlug(),
            post.getContent(),
            post.getExcerpt(),
            post.getAuthor(),
            post.getViewCount(),
            post.getLikeCount(),
            post.getPublished(),
            post.getPublishedAt(),
            post.getCreatedAt()
        );
    }
}

