package cm.dolers.laine_deco.application.usecase.impl;

import cm.dolers.laine_deco.application.dto.CategoryWithCountResponse;
import cm.dolers.laine_deco.application.dto.BrandWithCountResponse;
import cm.dolers.laine_deco.application.dto.SidebarFiltersResponse;
import cm.dolers.laine_deco.application.usecase.SidebarFiltersService;
import cm.dolers.laine_deco.infrastructure.persistence.entity.ProductEntity;
import cm.dolers.laine_deco.infrastructure.persistence.repository.ProductJpaRepository;
import cm.dolers.laine_deco.infrastructure.persistence.repository.CategoryJpaRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

@Transactional(readOnly = true)
public class SidebarFiltersServiceImpl implements SidebarFiltersService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SidebarFiltersServiceImpl.class);
    private final ProductJpaRepository ProductJpaRepository;
    private final CategoryJpaRepository CategoryJpaRepository;

    @Override
    public List<CategoryWithCountResponse> getCategoriesWithCount() {
        log.info("Fetching categories with product count");

        return CategoryJpaRepository.findAll().stream()
                .map(category -> {
                    long productCount = ProductJpaRepository.countByCategory(category);
                    return new CategoryWithCountResponse(
                            category.getId(),
                            category.getName(),
                            category.getDescription(),
                            productCount,
                            category.getImage());
                })
                .filter(cat -> cat.productCount() > 0) // Afficher seulement les catégories avec produits
                .collect(Collectors.toList());
    }

    @Override
    public List<BrandWithCountResponse> getBrandsWithCount() {
        log.info("Fetching brands with product count");

        return ProductJpaRepository.findAll().stream()
                .filter(p -> p.getBrand() != null && !p.getBrand().isBlank())
                .collect(Collectors.groupingBy(
                        ProductEntity::getBrand,
                        Collectors.counting()))
                .entrySet().stream()
                .map(entry -> new BrandWithCountResponse(entry.getKey(), entry.getValue()))
                .sorted((a, b) -> Long.compare(b.productCount(), a.productCount()))
                .collect(Collectors.toList());
    }

    @Override
    public SidebarFiltersResponse getSidebarFilters() {
        log.info("Fetching complete sidebar filters");

        List<CategoryWithCountResponse> categories = getCategoriesWithCount();
        List<BrandWithCountResponse> brands = getBrandsWithCount();
        Long totalProducts = getTotalProductCount();
        Long productsInStock = getInStockProductCount();

        return new SidebarFiltersResponse(categories, brands, totalProducts, productsInStock);
    }

    @Override
    public Long getTotalProductCount() {
        return ProductJpaRepository.count();
    }

    @Override
    public Long getInStockProductCount() {
        return ProductJpaRepository.count(org.springframework.data.jpa.domain.Specification.where(
                (root, query, cb) -> cb.greaterThan(root.get("stockQuantity"), 0)));
    }
}
