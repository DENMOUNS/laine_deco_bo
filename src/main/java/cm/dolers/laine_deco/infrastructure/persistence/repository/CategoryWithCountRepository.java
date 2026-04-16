package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryWithCountRepository extends JpaRepository<CategoryEntity, Long> {

    /**
     * Récupère toutes les catégories avec le count de produits pour chacune
     */
    @Query("SELECT c.id as id, c.name as name, c.description as description, " +
           "COUNT(p.id) as productCount, c.imageUrl as imageUrl " +
           "FROM CategoryEntity c LEFT JOIN ProductEntity p ON p.category.id = c.id " +
           "GROUP BY c.id, c.name, c.description, c.imageUrl " +
           "ORDER BY c.name ASC")
    List<?> findCategoriesWithProductCount();
}
