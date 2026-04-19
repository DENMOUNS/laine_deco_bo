package cm.dolers.laine_deco.interfaces.rest.controller.admin;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.BlogPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Admin Controller pour la gestion des posts de blog
 */
@RestController
@RequestMapping("/api/admin/blog-posts")
@RequiredArgsConstructor

@PreAuthorize("hasRole('ADMIN')")
public class AdminBlogPostController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AdminBlogPostController.class);
    private final BlogPostService blogPostService;

    @PostMapping
    public ResponseEntity<BlogPostResponse> createBlogPost(
            @Valid @RequestBody CreateBlogPostRequest request) {
        log.info("POST /api/admin/blog-posts - Creating: {}", request.title());
        var response = blogPostService.createBlogPost(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogPostResponse> getBlogPost(@PathVariable Long id) {
        log.info("GET /api/admin/blog-posts/{}", id);
        var response = blogPostService.getBlogPostById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<BlogPostResponse> getBlogPostBySlug(@PathVariable String slug) {
        log.info("GET /api/admin/blog-posts/slug/{}", slug);
        var response = blogPostService.getBlogPostBySlug(slug);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<BlogPostResponse>> getAllPosts(Pageable pageable) {
        log.info("GET /api/admin/blog-posts");
        var response = blogPostService.getAllPosts(pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogPostResponse> updateBlogPost(
            @PathVariable Long id,
            @Valid @RequestBody CreateBlogPostRequest request) {
        log.info("PUT /api/admin/blog-posts/{}", id);
        var response = blogPostService.updateBlogPost(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlogPost(@PathVariable Long id) {
        log.info("DELETE /api/admin/blog-posts/{}", id);
        blogPostService.deleteBlogPost(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<Void> publishPost(@PathVariable Long id) {
        log.info("POST /api/admin/blog-posts/{}/publish", id);
        blogPostService.publishPost(id);
        return ResponseEntity.ok().build();
    }
}
