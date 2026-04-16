package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.CategoryWithCountResponse;
import cm.dolers.laine_deco.application.dto.BrandWithCountResponse;
import cm.dolers.laine_deco.application.dto.SidebarFiltersResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service pour récupérer les données de filtrage du sidebar
 */
public interface SidebarFiltersService {

    /**
     * Récupère toutes les catégories avec le compte de produits pour chacune
     */
    List<CategoryWithCountResponse> getCategoriesWithCount();

    /**
     * Récupère toutes les marques avec le compte de produits pour chacune
     */
    List<BrandWithCountResponse> getBrandsWithCount();

    /**
     * Récupère les filtres complets du sidebar
     */
    SidebarFiltersResponse getSidebarFilters();

    /**
     * Récupère le count total de produits
     */
    Long getTotalProductCount();

    /**
     * Récupère le count de produits en stock
     */
    Long getInStockProductCount();
}
