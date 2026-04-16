package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.domain.model.Role;
import cm.dolers.laine_deco.infrastructure.persistence.entity.UserEntity;
import cm.dolers.laine_deco.infrastructure.persistence.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleJpaRepository extends JpaRepository<UserRoleEntity, Long> {
    List<UserRoleEntity> findByUserId(Long userId);

    Optional<UserRoleEntity> findByUserIdAndRole(Long userId, Role role);

    void deleteByUserIdAndRole(Long userId, Role role);

    void deleteByUserId(Long userId);

    List<UserRoleEntity> findByRole(Role role);
}
