package cm.dolers.laine_deco.interfaces.rest.controller.client;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.KnittingProjectService;
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
 * Client Controller pour la gestion des projets tricot
 */
@RestController
@RequestMapping("/api/client/knitting-projects")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('CLIENT')")
public class ClientKnittingProjectController {
    private final KnittingProjectService projectService;

    @PostMapping
    public ResponseEntity<KnittingProjectResponse> createProject(
            @Valid @RequestBody CreateKnittingProjectRequest request) {
        log.info("POST /api/client/knitting-projects - Creating: {}", request.name());
        var response = projectService.createProject(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KnittingProjectResponse> getProject(@PathVariable Long id) {
        log.info("GET /api/client/knitting-projects/{}", id);
        var response = projectService.getProjectById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<KnittingProjectResponse>> getMyProjects(
            Pageable pageable,
            HttpServletRequest request) {
        Long userId = extractUserIdFromToken(request);
        log.info("GET /api/client/knitting-projects - User: {}", userId);
        var response = projectService.getUserProjects(userId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    public ResponseEntity<?> getActiveProjects(HttpServletRequest request) {
        Long userId = extractUserIdFromToken(request);
        log.info("GET /api/client/knitting-projects/active - User: {}", userId);
        var response = projectService.getUserActiveProjects(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<KnittingProjectResponse> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody CreateKnittingProjectRequest request) {
        log.info("PUT /api/client/knitting-projects/{}", id);
        var response = projectService.updateProject(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/progress")
    public ResponseEntity<Void> updateProgress(
            @PathVariable Long id,
            @RequestParam Integer progress) {
        log.info("PUT /api/client/knitting-projects/{}/progress - {}", id, progress);
        projectService.updateProjectProgress(id, progress);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<Void> completeProject(@PathVariable Long id) {
        log.info("POST /api/client/knitting-projects/{}/complete", id);
        projectService.completeProject(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        log.info("DELETE /api/client/knitting-projects/{}", id);
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    private Long extractUserIdFromToken(HttpServletRequest request) {
        // TODO: Implémenter correctement
        return 1L;
    }
}
