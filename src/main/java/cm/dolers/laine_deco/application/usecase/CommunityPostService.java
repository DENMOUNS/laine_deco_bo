package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service pour Community Posts
 */
public interface CommunityPostService {
    CommunityPostResponse createPost(CreateCommunityPostRequest request);
    CommunityPostResponse getPostById(Long postId);
    Page<CommunityPostResponse> getAllPosts(Pageable pageable);
    Page<CommunityPostResponse> getUserPosts(Long userId, Pageable pageable);
    CommunityPostResponse updatePost(Long postId, CreateCommunityPostRequest request);
    void deletePost(Long postId);
    void likePost(Long postId);
    Long getPostLikesCount(Long postId);
    CommunityCommentResponse addComment(Long postId, String comment);
    Page<CommunityCommentResponse> getPostComments(Long postId, Pageable pageable);

    Page<CommunityPostResponse> getPublishedPosts(Pageable pageable);

    Page<CommunityPostResponse> getPublicPosts(Pageable pageable);
}



