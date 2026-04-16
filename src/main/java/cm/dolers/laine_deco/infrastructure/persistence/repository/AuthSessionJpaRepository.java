package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.AuthSessionEntity;
import cm.dolers.laine_deco.infrastructure.persistence.entity.UserEntity;
import java.time.Instant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthSessionJpaRepository extends JpaRepository<AuthSessionEntity, Long> {
    Optional<AuthSessionEntity> findByToken(String token);
    void deleteByToken(String token);
    void deleteByUser(UserEntity user);
    void deleteByExpiresAtBefore(Instant time);
}
