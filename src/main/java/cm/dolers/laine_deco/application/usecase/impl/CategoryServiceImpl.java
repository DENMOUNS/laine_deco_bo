package cm.dolers.laine_deco.application.usecase.impl;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.mapper.CategoryMapper;
import cm.dolers.laine_deco.application.usecase.CategoryService;
import cm.dolers.laine_deco.domain.exception.ErrorCode;
import cm.dolers.laine_deco.domain.exception.ProductException;
import cm.dolers.laine_deco.infrastructure.persistence.entity.CategoryEntity;
import cm.dolers.laine_deco.infrastructure.persistence.repository.CategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryJpaRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        log.info("Creating category: {}", request.name());

        if (categoryRepository.existsByName(request.name())) {
            throw new ProductException(ErrorCode.CATEGORY_ALREADY_EXISTS, "Name: " + request.name());
        }

        try {
            var category = new CategoryEntity();
            category.setName(request.name());
            category.setDescription(request.description());

            var saved = categoryRepository.save(category);
            log.info("Category created: {}", saved.getId());
            return categoryMapper.toResponse(saved);
        } catch (Exception ex) {
            log.error("Error creating category", ex);
            throw new ProductException(ErrorCode.OPERATION_FAILED, "Error creating category", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long categoryId) {
        var category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ProductException(ErrorCode.CATEGORY_NOT_FOUND, "ID: " + categoryId));
        return categoryMapper.toResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryResponse> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable)
            .map(categoryMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
            .stream()
            .map(categoryMapper::toResponse)
            .toList();
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long categoryId, CreateCategoryRequest request) {
        var category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ProductException(ErrorCode.CATEGORY_NOT_FOUND, "ID: " + categoryId));

        category.setName(request.name());
        category.setDescription(request.description());

        var updated = categoryRepository.save(category);
        log.info("Category updated: {}", categoryId);
        return categoryMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ProductException(ErrorCode.CATEGORY_NOT_FOUND, "ID: " + categoryId);
        }
        categoryRepository.deleteById(categoryId);
        log.info("Category deleted: {}", categoryId);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryByName(String name) {
        var category = categoryRepository.findByName(name)
            .orElseThrow(() -> new ProductException(ErrorCode.CATEGORY_NOT_FOUND, "Name: " + name));
        return categoryMapper.toResponse(category);
    }
}
