package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.FAQEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FAQJpaRepository extends JpaRepository<FAQEntity, Long> {
    List<FAQEntity> findAllByIsActiveTrueOrderByDisplayOrder();

    List<FAQEntity> findAllByIsActiveFalse();

    List<FAQEntity> findAllByOrderByDisplayOrderDesc();
}

