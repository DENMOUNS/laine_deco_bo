package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interface Service pour Posts de Blog
 */
public interface BlogPostService {
    BlogPostResponse createBlogPost(CreateBlogPostRequest request);
    BlogPostResponse getBlogPostById(Long postId);
    BlogPostResponse getBlogPostBySlug(String slug);
    Page<BlogPostResponse> getAllPosts(Pageable pageable);
    Page<BlogPostResponse> getPublishedPosts(Pageable pageable);
    BlogPostResponse updateBlogPost(Long postId, CreateBlogPostRequest request);
    void deleteBlogPost(Long postId);
    void publishPost(Long postId);
    void incrementViewCount(Long postId);
    void incrementLikeCount(Long postId);
}

