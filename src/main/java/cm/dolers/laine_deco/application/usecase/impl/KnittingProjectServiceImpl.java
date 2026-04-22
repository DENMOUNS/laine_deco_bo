package cm.dolers.laine_deco.application.usecase.impl;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.mapper.KnittingProjectMapper;
import cm.dolers.laine_deco.application.usecase.KnittingProjectService;
import cm.dolers.laine_deco.domain.exception.ErrorCode;
import cm.dolers.laine_deco.domain.exception.UserException;
import cm.dolers.laine_deco.infrastructure.persistence.entity.KnittingProjectEntity;
import cm.dolers.laine_deco.infrastructure.persistence.repository.KnittingProjectJpaRepository;
import cm.dolers.laine_deco.infrastructure.persistence.repository.UserJpaRepository;
import cm.dolers.laine_deco.infrastructure.security.SecurityUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

public class KnittingProjectServiceImpl implements KnittingProjectService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(KnittingProjectServiceImpl.class);
    private final KnittingProjectJpaRepository projectRepository;
    private final UserJpaRepository userRepository;
    private final KnittingProjectMapper projectMapper;

    @Override
    @Transactional
    public KnittingProjectResponse createProject(CreateKnittingProjectRequest request) {
        log.info("Creating knitting project: {}", request.name());

        try {
            Long userId = getCurrentUserId();
            var user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, "ID: " + userId));

            var project = new KnittingProjectEntity();
            project.setUser(user);
            project.setName(request.name());
            project.setDescription(request.description());
            project.setYarnColor(request.yarnColor());
            project.setYarnType(request.yarnType());
            project.setNeedleSize(String.valueOf(request.needleSize()));
            project.setDifficulty(request.difficulty());
            project.setEstimatedHours(request.estimatedHours() != null ? request.estimatedHours() : 0);
            project.setStatus("IN_PROGRESS");
            project.setProgress(0);

            var saved = projectRepository.save(project);
            log.info("Knitting project created: {}", saved.getId());
            return projectMapper.toResponse(saved);
        } catch (Exception ex) {
            log.error("Error creating knitting project", ex);
            throw new UserException(ErrorCode.OPERATION_FAILED, "Error creating project", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public KnittingProjectResponse getProjectById(Long projectId) {
        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> new UserException(ErrorCode.PROJECT_NOT_FOUND, "ID: " + projectId));
        return projectMapper.toResponse(project);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<KnittingProjectResponse> getUserProjects(Long userId, Pageable pageable) {
        return projectRepository.findByUserId(userId, pageable)
                .map(projectMapper::toResponse);
    }

    @Override
    @Transactional
    public KnittingProjectResponse updateProject(Long projectId, CreateKnittingProjectRequest request) {
        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> new UserException(ErrorCode.PROJECT_NOT_FOUND, "ID: " + projectId));

        project.setName(request.name());
        project.setDescription(request.description());
        project.setYarnColor(request.yarnColor());
        project.setYarnType(request.yarnType());
        project.setNeedleSize(String.valueOf(request.needleSize()));
        project.setDifficulty(request.difficulty());
        project.setEstimatedHours(request.estimatedHours() != null ? request.estimatedHours() : 0);

        var updated = projectRepository.save(project);
        log.info("Knitting project updated: {}", projectId);
        return projectMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteProject(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new UserException(ErrorCode.PROJECT_NOT_FOUND, "ID: " + projectId);
        }
        projectRepository.deleteById(projectId);
        log.info("Knitting project deleted: {}", projectId);
    }

    @Override
    @Transactional
    public void updateProjectProgress(Long projectId, Integer progress) {
        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> new UserException(ErrorCode.PROJECT_NOT_FOUND, "ID: " + projectId));
        project.setProgress(Math.min(progress, 100));
        projectRepository.save(project);
        log.info("Project progress updated: {} - {}", projectId, progress);
    }

    @Override
    @Transactional
    public void completeProject(Long projectId) {
        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> new UserException(ErrorCode.PROJECT_NOT_FOUND, "ID: " + projectId));
        project.setStatus("COMPLETED");
        project.setProgress(100);
        projectRepository.save(project);
        log.info("Knitting project completed: {}", projectId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<KnittingProjectResponse> getUserActiveProjects(Long userId) {
        return projectRepository.findByUserIdAndStatus(userId, "IN_PROGRESS")
                .stream()
                .map(projectMapper::toResponse)
                .toList();
    }

    private Long getCurrentUserId() {
        return SecurityUtils.getCurrentUserId();
    }
}
