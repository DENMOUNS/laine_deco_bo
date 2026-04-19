package cm.dolers.laine_deco.application.dto;

import java.time.Instant;

public record RmaResponse(
        Long id,
        Long orderId,
        String rmaNumber,
        String reason,
        String status,
        String trackingNumber,
        Instant createdAt,
        Instant resolvedAt) {
}
