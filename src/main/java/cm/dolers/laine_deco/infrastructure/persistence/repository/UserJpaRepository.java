package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    
    @EntityGraph(attributePaths = "roles")
    Optional<UserEntity> findByEmail(String email);

    @org.springframework.data.jpa.repository.Query("SELECT u FROM UserEntity u JOIN u.roles r WHERE r.role = :role")
    java.util.List<cm.dolers.laine_deco.infrastructure.persistence.entity.UserEntity> findByRole(@org.springframework.data.repository.query.Param("role") cm.dolers.laine_deco.domain.model.Role role);

    boolean existsByEmailIgnoreCase(String email);

    java.util.Optional<cm.dolers.laine_deco.infrastructure.persistence.entity.UserEntity> findByEmailIgnoreCase(String email);
}



