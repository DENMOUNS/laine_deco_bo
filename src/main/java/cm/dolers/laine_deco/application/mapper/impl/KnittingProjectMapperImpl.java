package cm.dolers.laine_deco.application.mapper.impl;

import cm.dolers.laine_deco.application.dto.KnittingProjectResponse;
import cm.dolers.laine_deco.application.mapper.KnittingProjectMapper;
import cm.dolers.laine_deco.infrastructure.persistence.entity.KnittingProjectEntity;
import java.util.List;
import java.util.stream.Collectors;
import java.time.ZoneOffset;
import org.springframework.stereotype.Component;

@Component
public class KnittingProjectMapperImpl implements KnittingProjectMapper {
    @Override
    public KnittingProjectResponse toResponse(KnittingProjectEntity entity) {
        return new KnittingProjectResponse(
            entity.getId(),
            entity.getName(),
            entity.getStatus(),
            null, // difficulty
            entity.getStartDate() != null ? entity.getStartDate().atStartOfDay().toInstant(ZoneOffset.UTC) : null,
            null // endDate
        );
    }

    public List<KnittingProjectResponse> toResponseList(List<KnittingProjectEntity> entities) {
        return entities.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
