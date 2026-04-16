package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.domain.model.LegalPageType;
import cm.dolers.laine_deco.infrastructure.persistence.entity.LegalPageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LegalPageRepository extends JpaRepository<LegalPageEntity, Long> {
    Optional<LegalPageEntity> findByType(LegalPageType type);

    List<LegalPageEntity> findAllByIsPublishedTrue();

    List<LegalPageEntity> findAllByOrderByUpdatedAtDesc();
}
