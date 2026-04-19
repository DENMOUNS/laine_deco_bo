package cm.dolers.laine_deco.interfaces.rest.controller.admin;

import cm.dolers.laine_deco.application.dto.CreateFAQRequest;
import cm.dolers.laine_deco.application.dto.FAQResponse;
import cm.dolers.laine_deco.application.usecase.FAQService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/faqs")
@RequiredArgsConstructor
public class AdminFAQController {
    private final FAQService faqService;

    @PostMapping
    public ResponseEntity<FAQResponse> createFAQ(@RequestBody CreateFAQRequest request) {
        FAQResponse response = faqService.createFAQ(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{faqId}")
    public ResponseEntity<FAQResponse> updateFAQ(@PathVariable Long faqId, @RequestBody CreateFAQRequest request) {
        FAQResponse response = faqService.updateFAQ(faqId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{faqId}")
    public ResponseEntity<FAQResponse> getFAQById(@PathVariable Long faqId) {
        FAQResponse response = faqService.getFAQById(faqId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<FAQResponse>> getAllFAQs() {
        List<FAQResponse> faqs = faqService.getAllFAQs();
        return ResponseEntity.ok(faqs);
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<FAQResponse>> getInactiveFAQs() {
        List<FAQResponse> faqs = faqService.getAllInactiveFAQs();
        return ResponseEntity.ok(faqs);
    }

    @PutMapping("/{faqId}/activate")
    public ResponseEntity<Void> activateFAQ(@PathVariable Long faqId) {
        faqService.activateFAQ(faqId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{faqId}/deactivate")
    public ResponseEntity<Void> deactivateFAQ(@PathVariable Long faqId) {
        faqService.deactivateFAQ(faqId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{faqId}/order")
    public ResponseEntity<Void> updateFAQOrder(@PathVariable Long faqId, @RequestParam Integer newOrder) {
        faqService.updateFAQOrder(faqId, newOrder);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{faqId}")
    public ResponseEntity<Void> deleteFAQ(@PathVariable Long faqId) {
        faqService.deleteFAQ(faqId);
        return ResponseEntity.noContent().build();
    }
}


