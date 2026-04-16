package cm.dolers.laine_deco.interfaces.rest.controller.client;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.CommunityPostService;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Client Controller pour Community Posts
 * Responsabilité: Créer/gérer les posts personnels et commenter les posts publics
 */
@RestController
@RequestMapping("/api/client/community-posts")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('CLIENT')")
public class ClientCommunityPostController {
    private final CommunityPostService postService;

    @PostMapping
    public ResponseEntity<CommunityPostResponse> createPost(@Valid @RequestBody CreateCommunityPostRequest request) {
        log.info("POST /api/client/community-posts - Creating");
        var response = postService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommunityPostResponse> getPost(@PathVariable Long id) {
        log.info("GET /api/client/community-posts/{}", id);
        var response = postService.getPostById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<Page<CommunityPostResponse>> getMyPosts(Pageable pageable,
            HttpServletRequest request) {
        Long userId = extractUserIdFromToken(request);
        log.info("GET /api/client/community-posts/me - User: {}", userId);
        var response = postService.getUserPosts(userId, pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommunityPostResponse> updatePost(@PathVariable Long id,
            @Valid @RequestBody CreateCommunityPostRequest request) {
        log.info("PUT /api/client/community-posts/{}", id);
        var response = postService.updatePost(id, request);
        return ResponseEntity.ok(response);
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
        var response = postService.getPostComments(id, pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<CommunityCommentResponse> addComment(@PathVariable Long id,
            @RequestParam String comment) {
        log.info("POST /api/client/community-posts/{}/comments", id);
        var response = postService.addComment(id, comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private Long extractUserIdFromToken(HttpServletRequest request) {
        // TODO: Implémenter correctement avec JWT
        return 1L;
    }
}
