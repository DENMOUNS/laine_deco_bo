package cm.dolers.laine_deco.application.usecase.impl;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.mapper.InvoiceMapper;
import cm.dolers.laine_deco.application.usecase.InvoiceService;
import cm.dolers.laine_deco.domain.exception.ErrorCode;
import cm.dolers.laine_deco.domain.exception.ValidationException;
import cm.dolers.laine_deco.infrastructure.persistence.entity.*;
import cm.dolers.laine_deco.infrastructure.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceJpaRepository invoiceRepository;
    private final OrderJpaRepository orderRepository;
    private final InvoiceMapper invoiceMapper;

    @Override
    @Transactional
    public InvoiceResponse generateInvoice(Long orderId) {
        log.info("Generating invoice for order: {}", orderId);

        var order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ValidationException(ErrorCode.ORDER_NOT_FOUND, "ID: " + orderId));

        try {
            var invoice = new InvoiceEntity();
            invoice.setOrder(order);
            invoice.setInvoiceNumber("INV-" + System.currentTimeMillis());
            invoice.setSubtotal(order.getTotal());
            invoice.setTaxAmount(order.getTax());
            invoice.setTotalAmount(order.getTotal().add(order.getTax()));
            invoice.setStatus("ISSUED");
            invoice.setIssuedAt(Instant.now());
            invoice.setDueDate(Instant.now().plusSeconds(30 * 24 * 60 * 60)); // 30 days

            var saved = invoiceRepository.save(invoice);
            log.info("Invoice generated: {}", saved.getId());
            return invoiceMapper.toResponse(saved);
        } catch (Exception ex) {
            log.error("Error generating invoice", ex);
            throw new ValidationException(ErrorCode.OPERATION_FAILED, "Error generating invoice", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse getInvoiceById(Long invoiceId) {
        var invoice = invoiceRepository.findById(invoiceId)
            .orElseThrow(() -> new ValidationException(ErrorCode.INVOICE_NOT_FOUND, "ID: " + invoiceId));
        return invoiceMapper.toResponse(invoice);
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse getInvoiceByNumber(String invoiceNumber) {
        var invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber)
            .orElseThrow(() -> new ValidationException(ErrorCode.INVOICE_NOT_FOUND, "Number: " + invoiceNumber));
        return invoiceMapper.toResponse(invoice);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceResponse> getOrderInvoices(Long orderId) {
        var invoices = invoiceRepository.findByOrderId(orderId);
        var responses = invoices.stream().map(invoiceMapper::toResponse).toList();
        return new PageImpl<>(responses, PageRequest.of(0, 10), responses.size());
    }

    @Override
    @Transactional
    public InvoiceResponse updateInvoiceStatus(Long invoiceId, String status) {
        var invoice = invoiceRepository.findById(invoiceId)
            .orElseThrow(() -> new ValidationException(ErrorCode.INVOICE_NOT_FOUND, "ID: " + invoiceId));
        invoice.setStatus(status);
        var updated = invoiceRepository.save(invoice);
        log.info("Invoice status updated: {} -> {}", invoiceId, status);
        return invoiceMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void sendInvoiceToEmail(Long invoiceId, String email) {
        var invoice = invoiceRepository.findById(invoiceId)
            .orElseThrow(() -> new ValidationException(ErrorCode.INVOICE_NOT_FOUND, "ID: " + invoiceId));
        // TODO: Intégrer avec service d'email
        log.info("Invoice {} sent to email: {}", invoiceId, email);
    }
}
