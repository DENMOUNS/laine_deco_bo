package cm.dolers.laine_deco.infrastructure.persistence.entity;

import cm.dolers.laine_deco.domain.model.LoyaltyTier;
import jakarta.persistence.*;
import java.time.Instant;

/**
 * Profil de loyauté utilisateur
 * Stocke les points cumulés et le tier de l'utilisateur
 */
@Entity
@Table(name = "user_loyalty_profiles")
public class UserLoyaltyProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "FK_loyalty_user_id"))
    private UserEntity user;

    @Column(name = "total_points", nullable = false)
    private Integer totalPoints = 0;

    @Column(name = "available_points", nullable = false)
    private Integer availablePoints = 0;     // Points utilisés par les rédemptions

    @Enumerated(EnumType.STRING)
    @Column(name = "current_tier", nullable = false)
    private LoyaltyTier currentTier = LoyaltyTier.STANDARD;

    @Column(name = "tier_reached_at")
    private Instant tierReachedAt;           // Date d'atteinte du tier actuel

    @Column(name = "total_spent", precision = 19, scale = 2)
    private java.math.BigDecimal totalSpent = java.math.BigDecimal.ZERO;

    @Column(name = "order_count")
    private Integer orderCount = 0;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Integer getAvailablePoints() {
        return availablePoints;
    }

    public void setAvailablePoints(Integer availablePoints) {
        this.availablePoints = availablePoints;
    }

    public LoyaltyTier getCurrentTier() {
        return currentTier;
    }

    public void setCurrentTier(LoyaltyTier currentTier) {
        this.currentTier = currentTier;
    }

    public Instant getTierReachedAt() {
        return tierReachedAt;
    }

    public void setTierReachedAt(Instant tierReachedAt) {
        this.tierReachedAt = tierReachedAt;
    }

    public java.math.BigDecimal getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(java.math.BigDecimal totalSpent) {
        this.totalSpent = totalSpent;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
