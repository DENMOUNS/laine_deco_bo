package cm.dolers.laine_deco.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderTrackingResponse {
    private Long orderId;
    private String orderNumber;
    private String status;
    private Long totalAmount;
    private Integer itemsCount;
    private String createdAt;
    private String estimatedDeliveryDate;
    private List<OrderStatusHistoryDto> statusHistory;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderStatusHistoryDto {
        private String status;
        private String changedAt;
        private String comment;
        private String changedBy;
    }
}
