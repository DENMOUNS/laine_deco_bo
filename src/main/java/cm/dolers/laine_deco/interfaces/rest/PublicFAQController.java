package cm.dolers.laine_deco.interfaces.rest;

import cm.dolers.laine_deco.application.dto.FAQResponse;
import cm.dolers.laine_deco.application.usecase.FAQService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/faqs")
@RequiredArgsConstructor
public class PublicFAQController {
    private final FAQService faqService;

    @GetMapping
    public ResponseEntity<List<FAQResponse>> getAllActiveFAQs() {
        List<FAQResponse> faqs = faqService.getAllActiveFAQs();
        return ResponseEntity.ok(faqs);
    }

    @GetMapping("/{faqId}")
    public ResponseEntity<FAQResponse> getFAQById(@PathVariable Long faqId) {
        FAQResponse response = faqService.getFAQById(faqId);
        return ResponseEntity.ok(response);
    }
}
