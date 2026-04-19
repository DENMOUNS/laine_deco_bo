package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.SiteConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteConfigJpaRepository extends JpaRepository<SiteConfigEntity, Long> {

    java.util.Optional<cm.dolers.laine_deco.infrastructure.persistence.entity.SiteConfigEntity> findByKey(String key);
}

