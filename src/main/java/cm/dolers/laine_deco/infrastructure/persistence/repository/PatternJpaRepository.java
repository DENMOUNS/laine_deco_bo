package cm.dolers.laine_deco.infrastructure.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cm.dolers.laine_deco.domain.model.SkillLevel;
import cm.dolers.laine_deco.infrastructure.persistence.entity.PatternEntity;

@Repository
public interface PatternJpaRepository extends JpaRepository<PatternEntity, Long> {

    @Query("SELECT p FROM PatternEntity p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(p.author) LIKE LOWER(CONCAT('%', :author, '%')) ORDER BY p.name ASC")
    Page<PatternEntity> findByNameContainingIgnoreCaseOrAuthorContainingIgnoreCase(@Param("name") String name, @Param("author") String author, Pageable pageable);
    
    @Query("SELECT p FROM PatternEntity p WHERE p.skillLevel = :skillLevel ORDER BY p.name ASC")
    Page<PatternEntity> findBySkillLevel(@Param("skillLevel") SkillLevel skillLevel, Pageable pageable);
}


