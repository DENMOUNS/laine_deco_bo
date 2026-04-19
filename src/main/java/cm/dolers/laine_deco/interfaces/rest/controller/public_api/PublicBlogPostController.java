package cm.dolers.laine_deco.interfaces.rest.controller.public_api;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.BlogPostService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/blog")
@RequiredArgsConstructor

public class PublicBlogPostController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PublicBlogPostController.class);
    private final BlogPostService blogPostService;

    @GetMapping("/posts/{id}")
    public ResponseEntity<BlogPostResponse> getBlogPost(@PathVariable Long id) {
        log.info("GET /api/public/blog/posts/{}", id);
        var response = blogPostService.getBlogPostById(id);
        blogPostService.incrementViewCount(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/posts/slug/{slug}")
    public ResponseEntity<BlogPostResponse> getBlogPostBySlug(@PathVariable String slug) {
        log.info("GET /api/public/blog/posts/slug/{}", slug);
        var response = blogPostService.getBlogPostBySlug(slug);
        blogPostService.incrementViewCount(response.id());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/posts")
    public ResponseEntity<Page<BlogPostResponse>> getPublishedPosts(Pageable pageable) {
        log.info("GET /api/public/blog/posts");
        var response = blogPostService.getPublishedPosts(pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/posts/{id}/like")
    public ResponseEntity<Void> likeBlogPost(@PathVariable Long id) {
        log.info("POST /api/public/blog/posts/{}/like", id);
        blogPostService.incrementLikeCount(id);
        return ResponseEntity.ok().build();
    }
}
