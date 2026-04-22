package cm.dolers.laine_deco.interfaces.rest.controller.client;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.CommunityPostService;
import cm.dolers.laine_deco.infrastructure.security.SecurityUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/client/community-posts")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CLIENT')")
public class ClientCommunityPostController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ClientCommunityPostController.class);
    private final CommunityPostService postService;

    @PostMapping
    public ResponseEntity<CommunityPostResponse> createPost(@Valid @RequestBody CreateCommunityPostRequest request) {
        log.info("POST /api/client/community-posts - Creating");
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommunityPostResponse> getPost(@PathVariable Long id) {
        log.info("GET /api/client/community-posts/{}", id);
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @GetMapping
    public ResponseEntity<Page<CommunityPostResponse>> getAllPosts(Pageable pageable) {
        log.info("GET /api/client/community-posts");
        return ResponseEntity.ok(postService.getAllPosts(pageable));
    }

    @GetMapping("/me")
    public ResponseEntity<Page<CommunityPostResponse>> getMyPosts(Pageable pageable) {
        Long userId = SecurityUtils.getCurrentUserId();
        log.info("GET /api/client/community-posts/me - User: {}", userId);
        return ResponseEntity.ok(postService.getUserPosts(userId, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommunityPostResponse> updatePost(@PathVariable Long id,
            @Valid @RequestBody CreateCommunityPostRequest request) {
        log.info("PUT /api/client/community-posts/{}", id);
        return ResponseEntity.ok(postService.updatePost(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        log.info("DELETE /api/client/community-posts/{}", id);
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likePost(@PathVariable Long id) {
        log.info("POST /api/client/community-posts/{}/like", id);
        postService.likePost(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<Page<CommunityCommentResponse>> getPostComments(@PathVariable Long id, Pageable pageable) {
        log.info("GET /api/client/community-posts/{}/comments", id);
        return ResponseEntity.ok(postService.getPostComments(id, pageable));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<CommunityCommentResponse> addComment(@PathVariable Long id,
            @RequestParam String comment) {
        log.info("POST /api/client/community-posts/{}/comments", id);
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.addComment(id, comment));
    }
}
