package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.ProductResponse;
import cm.dolers.laine_deco.application.dto.ProductSearchCriteria;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Service pour la recherche performante de produits
 * Utilise Specifications JPA et critères avancés
 */
public interface ProductSearchService {
    /**
     * Recherche avancée avec critères multiples
     */
    Page<ProductResponse> searchByCriteria(ProductSearchCriteria criteria);

    /**
     * Recherche texte rapide avec pagination
     */
    Page<ProductResponse> searchByKeyword(String keyword, int page, int pageSize);

    /**
     * Produits populaires
     */
    Page<ProductResponse> getPopularProducts(int page, int pageSize);

    /**
     * Produits les mieux notés
     */
    Page<ProductResponse> getTopRatedProducts(Integer minRating, int page, int pageSize);

    /**
     * Nouveaux produits
     */
    Page<ProductResponse> getNewestProducts(int page, int pageSize);

    /**
     * Suggestions d'auto-complétion pour recherche
     */
    List<String> getSearchSuggestions(String prefix);

    /**
     * Produits par catégorie
     */
    Page<ProductResponse> getProductsByCategory(Long categoryId, int page, int pageSize);

    /**
     * Produits en stock
     */
    Page<ProductResponse> getInStockProducts(int page, int pageSize);
}
