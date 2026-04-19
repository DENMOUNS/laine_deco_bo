package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Interface Service pour Catégories
 */
public interface CategoryService {
    CategoryResponse createCategory(CreateCategoryRequest request);
    CategoryResponse getCategoryById(Long categoryId);
    Page<CategoryResponse> getAllCategories(Pageable pageable);
    List<CategoryResponse> getAllCategories();
    CategoryResponse updateCategory(Long categoryId, CreateCategoryRequest request);
    void deleteCategory(Long categoryId);
    CategoryResponse getCategoryByName(String name);
}

