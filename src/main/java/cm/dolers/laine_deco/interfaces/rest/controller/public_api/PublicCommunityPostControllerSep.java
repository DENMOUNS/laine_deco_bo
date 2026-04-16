package cm.dolers.laine_deco.interfaces.rest.controller.public_api;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.CommunityPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Public Controller pour Community Posts
 * Responsabilité: Afficher les posts publics et les commentaires
 */
@RestController
@RequestMapping("/api/public/community-posts")
@RequiredArgsConstructor
@Slf4j
public class PublicCommunityPostController {
    private final CommunityPostService postService;

    @GetMapping("/{id}")
    public ResponseEntity<CommunityPostResponse> getPost(@PathVariable Long id) {
        log.info("GET /api/public/community-posts/{}", id);
        var response = postService.getPostById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<CommunityPostResponse>> getPublicPosts(Pageable pageable) {
        log.info("GET /api/public/community-posts");
        var response = postService.getPublicPosts(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<Page<CommunityCommentResponse>> getPostComments(@PathVariable Long id, Pageable pageable) {
        log.info("GET /api/public/community-posts/{}/comments", id);
        var response = postService.getPostComments(id, pageable);
        return ResponseEntity.ok(response);
    }
}
