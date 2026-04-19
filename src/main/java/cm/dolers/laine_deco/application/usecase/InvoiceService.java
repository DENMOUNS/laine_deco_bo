package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.*;
import org.springframework.data.domain.Page;

/**
 * Service pour Invoices
 */
public interface InvoiceService {
    InvoiceResponse generateInvoice(Long orderId);
    InvoiceResponse getInvoiceById(Long invoiceId);
    InvoiceResponse getInvoiceByNumber(String invoiceNumber);
    Page<InvoiceResponse> getOrderInvoices(Long orderId);
    InvoiceResponse updateInvoiceStatus(Long invoiceId, String status);
    void sendInvoiceToEmail(Long invoiceId, String email);
}

