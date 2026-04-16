package cm.dolers.laine_deco.application.usecase.impl;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.mapper.BlogPostMapper;
import cm.dolers.laine_deco.application.usecase.BlogPostService;
import cm.dolers.laine_deco.domain.exception.ErrorCode;
import cm.dolers.laine_deco.domain.exception.ValidationException;
import cm.dolers.laine_deco.infrastructure.persistence.entity.BlogPostEntity;
import cm.dolers.laine_deco.infrastructure.persistence.repository.BlogPostJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.text.Normalizer;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlogPostServiceImpl implements BlogPostService {
    private final BlogPostJpaRepository blogPostRepository;
    private final BlogPostMapper blogPostMapper;

    @Override
    @Transactional
    public BlogPostResponse createBlogPost(CreateBlogPostRequest request) {
        log.info("Creating blog post: {}", request.title());

        try {
            var slug = generateSlug(request.title());
            var post = new BlogPostEntity();
            post.setTitle(request.title());
            post.setSlug(slug);
            post.setContent(request.content());
            post.setExcerpt(request.excerpt());
            post.setAuthor(request.author());
            post.setViewCount(0);
            post.setLikeCount(0);
            post.setPublished(false);

            var saved = blogPostRepository.save(post);
            log.info("Blog post created: {}", saved.getId());
            return blogPostMapper.toResponse(saved);
        } catch (Exception ex) {
            log.error("Error creating blog post", ex);
            throw new ValidationException(ErrorCode.OPERATION_FAILED, "Error creating blog post", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BlogPostResponse getBlogPostById(Long postId) {
        var post = blogPostRepository.findById(postId)
            .orElseThrow(() -> new ValidationException(ErrorCode.BLOG_POST_NOT_FOUND, "ID: " + postId));
        return blogPostMapper.toResponse(post);
    }

    @Override
    @Transactional(readOnly = true)
    public BlogPostResponse getBlogPostBySlug(String slug) {
        var post = blogPostRepository.findBySlug(slug)
            .orElseThrow(() -> new ValidationException(ErrorCode.BLOG_POST_NOT_FOUND, "Slug: " + slug));
        return blogPostMapper.toResponse(post);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlogPostResponse> getAllPosts(Pageable pageable) {
        return blogPostRepository.findAll(pageable)
            .map(blogPostMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlogPostResponse> getPublishedPosts(Pageable pageable) {
        return blogPostRepository.findByPublishedTrue(pageable)
            .map(blogPostMapper::toResponse);
    }

    @Override
    @Transactional
    public BlogPostResponse updateBlogPost(Long postId, CreateBlogPostRequest request) {
        var post = blogPostRepository.findById(postId)
            .orElseThrow(() -> new ValidationException(ErrorCode.BLOG_POST_NOT_FOUND, "ID: " + postId));

        post.setTitle(request.title());
        post.setSlug(generateSlug(request.title()));
        post.setContent(request.content());
        post.setExcerpt(request.excerpt());
        post.setAuthor(request.author());

        var updated = blogPostRepository.save(post);
        log.info("Blog post updated: {}", postId);
        return blogPostMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteBlogPost(Long postId) {
        if (!blogPostRepository.existsById(postId)) {
            throw new ValidationException(ErrorCode.BLOG_POST_NOT_FOUND, "ID: " + postId);
        }
        blogPostRepository.deleteById(postId);
        log.info("Blog post deleted: {}", postId);
    }

    @Override
    @Transactional
    public void publishPost(Long postId) {
        var post = blogPostRepository.findById(postId)
            .orElseThrow(() -> new ValidationException(ErrorCode.BLOG_POST_NOT_FOUND, "ID: " + postId));
        post.setPublished(true);
        post.setPublishedAt(Instant.now());
        blogPostRepository.save(post);
        log.info("Blog post published: {}", postId);
    }

    @Override
    @Transactional
    public void incrementViewCount(Long postId) {
        var post = blogPostRepository.findById(postId)
            .orElseThrow(() -> new ValidationException(ErrorCode.BLOG_POST_NOT_FOUND, "ID: " + postId));
        post.setViewCount(post.getViewCount() + 1);
        blogPostRepository.save(post);
    }

    @Override
    @Transactional
    public void incrementLikeCount(Long postId) {
        var post = blogPostRepository.findById(postId)
            .orElseThrow(() -> new ValidationException(ErrorCode.BLOG_POST_NOT_FOUND, "ID: " + postId));
        post.setLikeCount(post.getLikeCount() + 1);
        blogPostRepository.save(post);
        log.info("Blog post liked: {}", postId);
    }

    private String generateSlug(String title) {
        String normalized = Normalizer.normalize(title, Normalizer.Form.NFD)
            .replaceAll("[^\\p{ASCII}]", "")
            .toLowerCase()
            .replaceAll("[^a-z0-9]", "-")
            .replaceAll("-+", "-")
            .replaceAll("^-|-$", "");
        return normalized;
    }
}
