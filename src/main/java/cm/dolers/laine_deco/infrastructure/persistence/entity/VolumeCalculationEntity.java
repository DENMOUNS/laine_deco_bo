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
@Table(name = "volume_calculations")
public class VolumeCalculationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "calculation_date", nullable = false)
    private LocalDate calculationDate;

    @Column(nullable = false)
    private String shape;

    @Column(nullable = false)
    private String material;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal volume;

    @Column(name = "detail_total", precision = 19, scale = 2)
    private BigDecimal detailTotal;

    @Column(name = "detail_part_a", precision = 19, scale = 2)
    private BigDecimal detailPartA;

    @Column(name = "detail_part_b", precision = 19, scale = 2)
    private BigDecimal detailPartB;

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

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getDetailTotal() {
        return detailTotal;
    }

    public void setDetailTotal(BigDecimal detailTotal) {
        this.detailTotal = detailTotal;
    }

    public BigDecimal getDetailPartA() {
        return detailPartA;
    }

    public void setDetailPartA(BigDecimal detailPartA) {
        this.detailPartA = detailPartA;
    }

    public BigDecimal getDetailPartB() {
        return detailPartB;
    }

    public void setDetailPartB(BigDecimal detailPartB) {
        this.detailPartB = detailPartB;
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
