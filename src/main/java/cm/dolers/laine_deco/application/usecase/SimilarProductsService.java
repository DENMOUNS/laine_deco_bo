package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.infrastructure.persistence.entity.ProductEntity;
import java.util.List;

/**
 * Service pour trouver les produits similaires
 */
public interface SimilarProductsService {

    /**
     * Récupère les produits similaires (entre 3 et 5)
     * Critères de similarité:
     * - Même catégorie (priorité haute)
     * - Même marque (priorité moyenne)
     * - Prix similaire (±20%)
     * - En stock de préférence
     * - Excluant le produit lui-même
     *
     * @param productId ID du produit
     * @param product Entité du produit
     * @return Liste de 3 à 5 produits similaires
     */
    List<ProductEntity> findSimilarProducts(Long productId, ProductEntity product);
}
