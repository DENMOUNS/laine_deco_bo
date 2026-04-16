package cm.dolers.laine_deco.infrastructure.persistence.entity;

import cm.dolers.laine_deco.domain.model.RewardType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * Représente une rédemption d'une récompense par un utilisateur
 * Exemple: Utilisateur a utilisé 500 points pour une livraison gratuite
 */
@Entity
@Table(name = "loyalty_redemptions")
public class LoyaltyRedemption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "FK_redemption_user_id"))
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(name = "reward_type", nullable = false)
    private RewardType rewardType;

    @Column(name = "points_used", nullable = false)
    private Integer pointsUsed;

    @Column(name = "reward_value", precision = 19, scale = 2)
    private BigDecimal rewardValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RedemptionStatus status = RedemptionStatus.PENDING;    // PENDING, APPLIED, EXPIRED

    @Column(name = "applied_at")
    private Instant appliedAt;                                      // Quand la récompense a été utilisée

    @Column(name = "expires_at")
    private Instant expiresAt;                                      // Expiration de la récompense (nullable = jamais expire)

    @Column(name = "reference_id")
    private Long referenceId;                                       // ID coupon/livraison si applicable

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

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

    public RewardType getRewardType() {
        return rewardType;
    }

    public void setRewardType(RewardType rewardType) {
        this.rewardType = rewardType;
    }

    public Integer getPointsUsed() {
        return pointsUsed;
    }

    public void setPointsUsed(Integer pointsUsed) {
        this.pointsUsed = pointsUsed;
    }

    public BigDecimal getRewardValue() {
        return rewardValue;
    }

    public void setRewardValue(BigDecimal rewardValue) {
        this.rewardValue = rewardValue;
    }

    public RedemptionStatus getStatus() {
        return status;
    }

    public void setStatus(RedemptionStatus status) {
        this.status = status;
    }

    public Instant getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(Instant appliedAt) {
        this.appliedAt = appliedAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Status d'une rédemption
     */
    public enum RedemptionStatus {
        PENDING,    // En attente d'être appliquée
        APPLIED,    // Appliquée/utilisée
        EXPIRED     // Expirée
    }
}
