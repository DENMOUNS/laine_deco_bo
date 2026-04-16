package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandWithCountRepository extends JpaRepository<ProductEntity, Long> {

    /**
     * Récupère toutes les marques avec le count de produits pour chacune
     */
    @Query("SELECT p.brand as brand, COUNT(p.id) as productCount " +
           "FROM ProductEntity p WHERE p.brand IS NOT NULL " +
           "GROUP BY p.brand " +
           "ORDER BY COUNT(p.id) DESC")
    List<?> findBrandsWithProductCount();
}
