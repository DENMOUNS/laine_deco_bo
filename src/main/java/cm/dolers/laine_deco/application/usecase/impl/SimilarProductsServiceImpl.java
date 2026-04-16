package cm.dolers.laine_deco.application.usecase.impl;

import cm.dolers.laine_deco.application.usecase.SimilarProductsService;
import cm.dolers.laine_deco.infrastructure.persistence.entity.ProductEntity;
import cm.dolers.laine_deco.infrastructure.persistence.repository.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SimilarProductsServiceImpl implements SimilarProductsService {
    private final ProductJpaRepository productRepository;

    private static final int MIN_SIMILAR_PRODUCTS = 3;
    private static final int MAX_SIMILAR_PRODUCTS = 5;
    private static final double PRICE_TOLERANCE = 0.2; // ±20%

    @Override
    public List<ProductEntity> findSimilarProducts(Long productId, ProductEntity product) {
        log.info("Finding similar products for productId: {}", productId);

        // Récupérer tous les produits sauf celui-ci
        List<ProductEntity> allProducts = productRepository.findAll().stream()
            .filter(p -> !p.getId().equals(productId))
            .collect(Collectors.toList());

        if (allProducts.isEmpty()) {
            log.warn("No products found to compare with productId: {}", productId);
            return List.of();
        }

        // Calculer un score de similarité pour chaque produit
        List<ProductWithSimilarityScore> productsWithScore = allProducts.stream()
            .map(p -> new ProductWithSimilarityScore(p, calculateSimilarityScore(product, p)))
            .filter(p -> p.score > 0)  // Garder seulement les produits avec un score positif
            .sorted(Comparator.comparingDouble(ProductWithSimilarityScore::score).reversed())
            .limit(MAX_SIMILAR_PRODUCTS)
            .collect(Collectors.toList());

        // S'assurer d'avoir au moins MIN_SIMILAR_PRODUCTS produits
        if (productsWithScore.size() < MIN_SIMILAR_PRODUCTS) {
            log.warn("Only found {} similar products for productId: {}, minimum is {}",
                productsWithScore.size(), productId, MIN_SIMILAR_PRODUCTS);
            // Ajouter les produits les plus vendus si nécessaire
            List<ProductEntity> topSellers = allProducts.stream()
                .filter(p -> productsWithScore.stream().noneMatch(ps -> ps.product.getId().equals(p.getId())))
                .filter(p -> p.getStockQuantity() > 0)  // Priorité aux produits en stock
                .sorted(Comparator.comparingInt(ProductEntity::getSalesCount).reversed())
                .limit(MIN_SIMILAR_PRODUCTS - productsWithScore.size())
                .collect(Collectors.toList());

            productsWithScore.addAll(topSellers.stream()
                .map(p -> new ProductWithSimilarityScore(p, 0.0))
                .collect(Collectors.toList()));
        }

        List<ProductEntity> result = productsWithScore.stream()
            .map(ProductWithSimilarityScore::product)
            .collect(Collectors.toList());

        log.info("Found {} similar products for productId: {}", result.size(), productId);
        return result;
    }

    /**
     * Calcule un score de similarité entre deux produits
     * Score basé sur:
     * - Même catégorie: +100 points
     * - Même marque: +50 points
     * - Prix similaire (±20%): +30 points
     * - En stock: +10 points
     */
    private double calculateSimilarityScore(ProductEntity original, ProductEntity candidate) {
        double score = 0.0;

        // Même catégorie (priorité haute)
        if (original.getCategory() != null && candidate.getCategory() != null &&
            original.getCategory().getId().equals(candidate.getCategory().getId())) {
            score += 100.0;
        }

        // Même marque (priorité moyenne)
        if (original.getBrand() != null && candidate.getBrand() != null &&
            original.getBrand().equalsIgnoreCase(candidate.getBrand())) {
            score += 50.0;
        }

        // Prix similaire ±20% (priorité basse)
        if (isPriceSimilar(original.getSalePrice(), candidate.getSalePrice())) {
            score += 30.0;
        }

        // Produit en stock (bonus)
        if (candidate.getStockQuantity() > 0) {
            score += 10.0;
        }

        // Bonus pour popularité (si le candidat a beaucoup de ventes)
        double salesScore = Math.min(candidate.getSalesCount() / 100.0, 20.0);
        score += salesScore;

        return score;
    }

    /**
     * Vérifie si deux prix sont similaires (±20%)
     */
    private boolean isPriceSimilar(BigDecimal price1, BigDecimal price2) {
        if (price1 == null || price2 == null || price1.signum() == 0) {
            return false;
        }

        BigDecimal minPrice = price1.multiply(BigDecimal.valueOf(1 - PRICE_TOLERANCE));
        BigDecimal maxPrice = price1.multiply(BigDecimal.valueOf(1 + PRICE_TOLERANCE));

        return price2.compareTo(minPrice) >= 0 && price2.compareTo(maxPrice) <= 0;
    }

    /**
     * Record interne pour associer un produit avec son score de similarité
     */
    private record ProductWithSimilarityScore(ProductEntity product, double score) {}
}
