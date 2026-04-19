package cm.dolers.laine_deco.interfaces.rest.controller.admin;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.CommunityPostService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Admin Controller pour Community Posts
 * Responsabilité: Modérer les posts de la communauté et les supprimer si
 * nécessaire
 */
@RestController
@RequestMapping("/api/admin/community-posts")
@RequiredArgsConstructor

@PreAuthorize("hasRole('ADMIN')")
public class AdminCommunityPostController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AdminCommunityPostController.class);
    private final CommunityPostService postService;

    @GetMapping("/{id}")
    public ResponseEntity<CommunityPostResponse> getPost(@PathVariable Long id) {
        log.info("GET /api/admin/community-posts/{}", id);
        var response = postService.getPostById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<CommunityPostResponse>> getAllPosts(Pageable pageable) {
        log.info("GET /api/admin/community-posts");
        var response = postService.getAllPosts(pageable);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        log.info("DELETE /api/admin/community-posts/{}", id);
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
