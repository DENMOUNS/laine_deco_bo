package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Interface Service pour Projets Tricot
 */
public interface KnittingProjectService {
    KnittingProjectResponse createProject(CreateKnittingProjectRequest request);
    KnittingProjectResponse getProjectById(Long projectId);
    Page<KnittingProjectResponse> getUserProjects(Long userId, Pageable pageable);
    KnittingProjectResponse updateProject(Long projectId, CreateKnittingProjectRequest request);
    void deleteProject(Long projectId);
    void updateProjectProgress(Long projectId, Integer progress);
    void completeProject(Long projectId);
    List<KnittingProjectResponse> getUserActiveProjects(Long userId);
}

