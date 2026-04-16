package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.ClientProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientProfileJpaRepository extends JpaRepository<ClientProfileEntity, Long> {
    Optional<ClientProfileEntity> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
