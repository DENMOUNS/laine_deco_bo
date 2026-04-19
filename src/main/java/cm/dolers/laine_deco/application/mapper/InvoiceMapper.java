package cm.dolers.laine_deco.application.mapper;

import cm.dolers.laine_deco.application.dto.InvoiceResponse;
import cm.dolers.laine_deco.infrastructure.persistence.entity.InvoiceEntity;

public interface InvoiceMapper {
    InvoiceResponse toResponse(InvoiceEntity invoice);
}

