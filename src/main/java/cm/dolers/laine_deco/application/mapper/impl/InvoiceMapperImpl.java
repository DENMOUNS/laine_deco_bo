package cm.dolers.laine_deco.application.mapper.impl;

import cm.dolers.laine_deco.application.dto.InvoiceResponse;
import cm.dolers.laine_deco.application.mapper.InvoiceMapper;
import cm.dolers.laine_deco.infrastructure.persistence.entity.InvoiceEntity;
import org.springframework.stereotype.Component;

@Component
public class InvoiceMapperImpl implements InvoiceMapper {
    @Override
    public InvoiceResponse toResponse(InvoiceEntity invoice) {
        return new InvoiceResponse(invoice.getId(), invoice.getOrder().getId(),
            invoice.getInvoiceNumber(), invoice.getSubtotal(), invoice.getTaxAmount(),
            invoice.getTotalAmount(), invoice.getStatus(), invoice.getIssuedAt(), invoice.getDueDate());
    }
}

