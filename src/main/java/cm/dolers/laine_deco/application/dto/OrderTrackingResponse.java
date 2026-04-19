package cm.dolers.laine_deco.application.dto;
import java.util.List;
public class OrderTrackingResponse {
    private Long orderId;
    private String orderNumber;
    private String status;
    private long totalAmount;
    private int itemsCount;
    private String createdAt;
    private String estimatedDeliveryDate;
    private List<OrderStatusHistoryDto> statusHistory;
    
    public Long getOrderId() { return orderId; } public void setOrderId(Long orderId) { this.orderId = orderId; }
    public String getOrderNumber() { return orderNumber; } public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public String getStatus() { return status; } public void setStatus(String status) { this.status = status; }
    public long getTotalAmount() { return totalAmount; } public void setTotalAmount(long totalAmount) { this.totalAmount = totalAmount; }
    public int getItemsCount() { return itemsCount; } public void setItemsCount(int itemsCount) { this.itemsCount = itemsCount; }
    public String getCreatedAt() { return createdAt; } public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getEstimatedDeliveryDate() { return estimatedDeliveryDate; } public void setEstimatedDeliveryDate(String estimatedDeliveryDate) { this.estimatedDeliveryDate = estimatedDeliveryDate; }
    public List<OrderStatusHistoryDto> getStatusHistory() { return statusHistory; } public void setStatusHistory(List<OrderStatusHistoryDto> statusHistory) { this.statusHistory = statusHistory; }

    public static class OrderStatusHistoryDto {
        private String status;
        private String changedAt;
        private String comment;
        private String changedBy;
        public OrderStatusHistoryDto(String s, String ca, String c, String cb) { this.status = s; this.changedAt = ca; this.comment = c; this.changedBy = cb; }
        public OrderStatusHistoryDto() {}
        public String getStatus() { return status; } public void setStatus(String status) { this.status = status; }
        public String getChangedAt() { return changedAt; } public void setChangedAt(String changedAt) { this.changedAt = changedAt; }
        public String getComment() { return comment; } public void setComment(String comment) { this.comment = comment; }
        public String getChangedBy() { return changedBy; } public void setChangedBy(String changedBy) { this.changedBy = changedBy; }
    }
}
