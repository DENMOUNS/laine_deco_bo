package cm.dolers.laine_deco.application.dto;

import java.util.List;

/**
 * DTO pour la réponse détaillée d'un produit avec produits similaires
 */
public record ProductDetailWithSimilarsResponse(
    ProductDetailResponse product,
    List<ProductResponse> similarProducts
) {}
