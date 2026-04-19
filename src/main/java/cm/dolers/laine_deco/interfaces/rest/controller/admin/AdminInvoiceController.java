package cm.dolers.laine_deco.interfaces.rest.controller.admin;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.InvoiceService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Admin Controller pour Invoices
 * Responsabilité: Générer les factures, gérer leur statut et les envoyer par
 * email
 */
@RestController
@RequestMapping("/api/admin/invoices")
@RequiredArgsConstructor

@PreAuthorize("hasAnyRole('ADMIN', 'FINANCE')")
public class AdminInvoiceController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AdminInvoiceController.class);
    private final InvoiceService invoiceService;

    @PostMapping("/orders/{orderId}")
    public ResponseEntity<InvoiceResponse> generateInvoice(@PathVariable Long orderId) {
        log.info("POST /api/admin/invoices/orders/{}", orderId);
        var response = invoiceService.generateInvoice(orderId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse> getInvoice(@PathVariable Long id) {
        log.info("GET /api/admin/invoices/{}", id);
        var response = invoiceService.getInvoiceById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/number/{invoiceNumber}")
    public ResponseEntity<InvoiceResponse> getInvoiceByNumber(@PathVariable String invoiceNumber) {
        log.info("GET /api/admin/invoices/number/{}", invoiceNumber);
        var response = invoiceService.getInvoiceByNumber(invoiceNumber);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<Page<InvoiceResponse>> getOrderInvoices(@PathVariable Long orderId) {
        log.info("GET /api/admin/invoices/order/{}", orderId);
        var response = invoiceService.getOrderInvoices(orderId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<InvoiceResponse> updateStatus(@PathVariable Long id, @RequestParam String status) {
        log.info("PUT /api/admin/invoices/{}/status - {}", id, status);
        var response = invoiceService.updateInvoiceStatus(id, status);
        return ResponseEntity.ok(response);
    }
}
