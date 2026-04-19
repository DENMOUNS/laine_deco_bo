package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.CreateFAQRequest;
import cm.dolers.laine_deco.application.dto.FAQResponse;

import java.util.List;

public interface FAQService {
    FAQResponse createFAQ(CreateFAQRequest request);
    FAQResponse updateFAQ(Long faqId, CreateFAQRequest request);
    FAQResponse getFAQById(Long faqId);
    List<FAQResponse> getAllActiveFAQs();
    List<FAQResponse> getAllFAQs();
    List<FAQResponse> getAllInactiveFAQs();
    void activateFAQ(Long faqId);
    void deactivateFAQ(Long faqId);
    void deleteFAQ(Long faqId);
    void updateFAQOrder(Long faqId, Integer newOrder);
}

