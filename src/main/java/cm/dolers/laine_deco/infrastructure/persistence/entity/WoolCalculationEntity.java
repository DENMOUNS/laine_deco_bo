package cm.dolers.laine_deco.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * WoolCalculationEntity
 *
 * CORRECTIONS :
 * - Suppression des champs persistés en doublon (projectType/size/result vs
 *   projectName/needleSize/estimatedYardage etc.)
 * - Un seul set de champs cohérent orienté calculateur de laine
 * - `user` : relation ManyToOne correctement mappée
 */
@Entity
@Table(name = "wool_calculations")
public class WoolCalculationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_wool_calculations_user_id"))
    private UserEntity user;

    @Column(name = "project_name", nullable = false, length = 200)
    private String projectName;

    @Column(name = "yarn_weight", nullable = false, length = 50)
    private String yarnWeight;

    @Column(name = "needle_size", length = 20)
    private String needleSize;

    @Column(name = "width_cm")
    private Double width;

    @Column(name = "height_cm")
    private Double height;

    @Column(name = "estimated_yardage")
    private Double estimatedYardage;

    @Column(name = "estimated_weight_grams")
    private Double estimatedWeight;

    @Column(name = "number_of_skeins")
    private Integer numberOfSkeins;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @PreUpdate
    public void preUpdate() {
        // updatedAt non stocké — la calculation ne change pas après création
    }

    // =================== GETTERS / SETTERS ===================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserEntity getUser() { return user; }
    public void setUser(UserEntity user) { this.user = user; }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public String getYarnWeight() { return yarnWeight; }
    public void setYarnWeight(String yarnWeight) { this.yarnWeight = yarnWeight; }

    public String getNeedleSize() { return needleSize; }
    public void setNeedleSize(String needleSize) { this.needleSize = needleSize; }

    public Double getWidth() { return width; }
    public void setWidth(Double width) { this.width = width; }

    public Double getHeight() { return height; }
    public void setHeight(Double height) { this.height = height; }

    public Double getEstimatedYardage() { return estimatedYardage; }
    public void setEstimatedYardage(Double estimatedYardage) { this.estimatedYardage = estimatedYardage; }

    public Double getEstimatedWeight() { return estimatedWeight; }
    public void setEstimatedWeight(Double estimatedWeight) { this.estimatedWeight = estimatedWeight; }

    public Integer getNumberOfSkeins() { return numberOfSkeins; }
    public void setNumberOfSkeins(Integer numberOfSkeins) { this.numberOfSkeins = numberOfSkeins; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
