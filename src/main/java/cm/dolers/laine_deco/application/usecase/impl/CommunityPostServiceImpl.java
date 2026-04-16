package cm.dolers.laine_deco.application.usecase.impl;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.mapper.CommunityPostMapper;
import cm.dolers.laine_deco.application.usecase.CommunityPostService;
import cm.dolers.laine_deco.domain.exception.ErrorCode;
import cm.dolers.laine_deco.domain.exception.UserException;
import cm.dolers.laine_deco.infrastructure.persistence.entity.*;
import cm.dolers.laine_deco.infrastructure.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommunityPostServiceImpl implements CommunityPostService {
    private final CommunityPostJpaRepository postRepository;
    private final CommunityCommentJpaRepository commentRepository;
    private final UserJpaRepository userRepository;
    private final CommunityPostMapper postMapper;

    @Override
    @Transactional
    public CommunityPostResponse createPost(CreateCommunityPostRequest request) {
        log.info("Creating community post");
        // TODO: Extraire userId du JWT
        Long userId = 1L;
        
        var user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, "ID: " + userId));

        try {
            var post = new CommunityPostEntity();
            post.setUser(user);
            post.setTitle(request.title());
            post.setContent(request.content());
            post.setLikesCount(0);
            post.setCommentsCount(0);
            post.setViewsCount(0);

            var saved = postRepository.save(post);
            log.info("Community post created: {}", saved.getId());
            return postMapper.toResponse(saved);
        } catch (Exception ex) {
            log.error("Error creating community post", ex);
            throw new UserException(ErrorCode.OPERATION_FAILED, "Error creating post", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CommunityPostResponse getPostById(Long postId) {
        var post = postRepository.findById(postId)
            .orElseThrow(() -> new UserException(ErrorCode.POST_NOT_FOUND, "ID: " + postId));
        return postMapper.toResponse(post);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommunityPostResponse> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable).map(postMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommunityPostResponse> getUserPosts(Long userId, Pageable pageable) {
        return postRepository.findByUserId(userId, pageable).map(postMapper::toResponse);
    }

    @Override
    @Transactional
    public CommunityPostResponse updatePost(Long postId, CreateCommunityPostRequest request) {
        var post = postRepository.findById(postId)
            .orElseThrow(() -> new UserException(ErrorCode.POST_NOT_FOUND, "ID: " + postId));
        
        post.setTitle(request.title());
        post.setContent(request.content());
        
        var updated = postRepository.save(post);
        log.info("Community post updated: {}", postId);
        return postMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deletePost(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new UserException(ErrorCode.POST_NOT_FOUND, "ID: " + postId);
        }
        postRepository.deleteById(postId);
        log.info("Community post deleted: {}", postId);
    }

    @Override
    @Transactional
    public void likePost(Long postId) {
        var post = postRepository.findById(postId)
            .orElseThrow(() -> new UserException(ErrorCode.POST_NOT_FOUND, "ID: " + postId));
        post.setLikesCount(post.getLikesCount() + 1);
        postRepository.save(post);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getPostLikesCount(Long postId) {
        var post = postRepository.findById(postId)
            .orElseThrow(() -> new UserException(ErrorCode.POST_NOT_FOUND, "ID: " + postId));
        return (long) post.getLikesCount();
    }

    @Override
    @Transactional
    public CommunityCommentResponse addComment(Long postId, String comment) {
        // TODO: Extraire userId du JWT
        Long userId = 1L;

        var post = postRepository.findById(postId)
            .orElseThrow(() -> new UserException(ErrorCode.POST_NOT_FOUND, "ID: " + postId));
        var user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, "ID: " + userId));

        try {
            var commentEntity = new CommunityCommentEntity();
            commentEntity.setPost(post);
            commentEntity.setUser(user);
            commentEntity.setComment(comment);
            commentEntity.setLikesCount(0);

            var saved = commentRepository.save(commentEntity);
            post.setCommentsCount(post.getCommentsCount() + 1);
            postRepository.save(post);

            log.info("Community comment added: {}", saved.getId());
            return postMapper.toCommentResponse(saved);
        } catch (Exception ex) {
            log.error("Error adding comment", ex);
            throw new UserException(ErrorCode.OPERATION_FAILED, "Error adding comment", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommunityCommentResponse> getPostComments(Long postId, Pageable pageable) {
        return commentRepository.findByPostId(postId, pageable).map(postMapper::toCommentResponse);
    }
}
