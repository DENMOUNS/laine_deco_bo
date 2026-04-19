package cm.dolers.laine_deco.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import cm.dolers.laine_deco.domain.model.RMAStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "rmas")
public class RMAEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id", nullable = false, foreignKey = @ForeignKey(name = "FK_rmas_order_id"))
    private OrderEntity order;

    @Column(nullable = false)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RMAStatus status = RMAStatus.PENDING;

    @Column(name = "rma_date", nullable = false)
    private LocalDate rmaDate;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal refundAmount;

    @Lob
    @Column
    private String internalNotes;

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

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public RMAStatus getStatus() {
        return status;
    }

    public void setStatus(RMAStatus status) {
        this.status = status;
    }

    public LocalDate getRmaDate() {
        return rmaDate;
    }

    public void setRmaDate(LocalDate rmaDate) {
        this.rmaDate = rmaDate;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getInternalNotes() {
        return internalNotes;
    }

    public void setInternalNotes(String internalNotes) {
        this.internalNotes = internalNotes;
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

    private java.time.Instant resolvedAt;
    private String trackingNumber;
    private String rmaNumber;
    public java.time.Instant getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(java.time.Instant resolvedAt) { this.resolvedAt = resolvedAt; }
    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }
    public String getRmaNumber() { return rmaNumber; }
    public void setRmaNumber(String rmaNumber) { this.rmaNumber = rmaNumber; }
}

