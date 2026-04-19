package cm.dolers.laine_deco.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "wool_calculations")
public class WoolCalculationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "calculation_date", nullable = false)
    private LocalDate calculationDate;

    @Column(nullable = false)
    private String projectType;

    @Column
    private String size;

    @Column(nullable = false)
    private String yarnWeight;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal result;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCalculationDate() {
        return calculationDate;
    }

    public void setCalculationDate(LocalDate calculationDate) {
        this.calculationDate = calculationDate;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getYarnWeight() {
        return yarnWeight;
    }

    public void setYarnWeight(String yarnWeight) {
        this.yarnWeight = yarnWeight;
    }

    public BigDecimal getResult() {
        return result;
    }

    public void setResult(BigDecimal result) {
        this.result = result;
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

    private String projectName;
    private String needleSize;
    private Double width;
    private Double height;
    private Double estimatedYardage;
    private Double estimatedWeight;
    private Integer numberOfSkeins;
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
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

    @jakarta.persistence.ManyToOne
    @jakarta.persistence.JoinColumn(name = "user_id")
    private UserEntity user;
    public UserEntity getUser() { return user; }
    public void setUser(UserEntity user) { this.user = user; }
}


