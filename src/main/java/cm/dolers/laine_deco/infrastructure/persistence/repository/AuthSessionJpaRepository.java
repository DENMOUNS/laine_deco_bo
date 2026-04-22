package cm.dolers.laine_deco.infrastructure.persistence.repository;

import cm.dolers.laine_deco.infrastructure.persistence.entity.AuthSessionEntity;
import cm.dolers.laine_deco.infrastructure.persistence.entity.UserEntity;
import java.time.Instant;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthSessionJpaRepository extends JpaRepository<AuthSessionEntity, Long> {
    
    @EntityGraph(attributePaths = {"user", "user.roles"})
    Optional<AuthSessionEntity> findByToken(String token);
    
    @Query("SELECT s FROM AuthSessionEntity s JOIN FETCH s.user u LEFT JOIN FETCH u.roles WHERE s.token = :token")
    Optional<AuthSessionEntity> findByTokenWithUserAndRoles(@Param("token") String token);
    
    void deleteByToken(String token);
    void deleteByUser(UserEntity user);
    void deleteByExpiresAtBefore(Instant time);
}
