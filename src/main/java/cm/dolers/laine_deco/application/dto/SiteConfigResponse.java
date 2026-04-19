package cm.dolers.laine_deco.application.dto;

import java.time.Instant;

public record SiteConfigResponse(
        Long id,
        String key,
        String value,
        String description,
        Instant updatedAt) {
}
