// BadgeResponse.java
package cm.dolers.laine_deco.application.dto;

import java.time.Instant;

public record BadgeResponse(
    Long id,
    String name,
    String description,
    String icon,
    String criteria,
    Instant createdAt
) {}
