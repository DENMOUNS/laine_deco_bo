package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.PatternEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatternJpaRepository extends JpaRepository<PatternEntity, Long> {

    org.springframework.data.domain.Page<cm.dolers.laine_deco.infrastructure.persistence.entity.PatternEntity> findByNameContainingIgnoreCaseOrAuthorContainingIgnoreCase(String n, String a, org.springframework.data.domain.Pageable p);
    org.springframework.data.domain.Page<cm.dolers.laine_deco.infrastructure.persistence.entity.PatternEntity> findBySkillLevel(cm.dolers.laine_deco.domain.model.SkillLevel s, org.springframework.data.domain.Pageable p);
}


