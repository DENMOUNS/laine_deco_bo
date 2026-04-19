package cm.dolers.laine_deco.application.dto;

import java.util.List;

/**
 * DTO pour le sidebar avec filtres (catégories, marques, etc.)
 */
public record SidebarFiltersResponse(
        List<CategoryWithCountResponse> categories,
        List<BrandWithCountResponse> brands,
        Long totalProducts,
        Long productsInStock) {
}
