package cm.dolers.laine_deco.application.mapper;

import cm.dolers.laine_deco.infrastructure.i18n.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Mapper pour fournir les labels traduits pour les réponses API
 */
@Component
@RequiredArgsConstructor
public class TranslationMapper {
    private final MessageService messageService;

    /**
     * Récupère les labels traduits pour les produits
     */
    public Map<String, String> getProductLabels() {
        Map<String, String> labels = new HashMap<>();
        labels.put("product.name", messageService.getMessage("product.name"));
        labels.put("product.description", messageService.getMessage("product.description"));
        labels.put("product.price", messageService.getMessage("product.price"));
        labels.put("product.sale.price", messageService.getMessage("product.sale.price"));
        labels.put("product.stock", messageService.getMessage("product.stock"));
        labels.put("product.brand", messageService.getMessage("product.brand"));
        labels.put("product.rating", messageService.getMessage("product.rating"));
        labels.put("product.is.new", messageService.getMessage("product.is.new"));
        labels.put("product.is.sale", messageService.getMessage("product.is.sale"));
        labels.put("product.is.available", messageService.getMessage("product.is.available"));
        return labels;
    }

    /**
     * Récupère les labels traduits pour les catégories
     */
    public Map<String, String> getCategoryLabels() {
        Map<String, String> labels = new HashMap<>();
        labels.put("category.name", messageService.getMessage("category.name"));
        labels.put("category.description", messageService.getMessage("category.description"));
        labels.put("category.products.count", messageService.getMessage("category.products.count"));
        return labels;
    }

    /**
     * Récupère les labels traduits pour les commandes
     */
    public Map<String, String> getOrderLabels() {
        Map<String, String> labels = new HashMap<>();
        labels.put("order.number", messageService.getMessage("order.number"));
        labels.put("order.status", messageService.getMessage("order.status"));
        labels.put("order.status.pending", messageService.getMessage("order.status.pending"));
        labels.put("order.status.confirmed", messageService.getMessage("order.status.confirmed"));
        labels.put("order.status.processing", messageService.getMessage("order.status.processing"));
        labels.put("order.status.shipped", messageService.getMessage("order.status.shipped"));
        labels.put("order.status.delivered", messageService.getMessage("order.status.delivered"));
        labels.put("order.status.cancelled", messageService.getMessage("order.status.cancelled"));
        labels.put("order.total.amount", messageService.getMessage("order.total.amount"));
        labels.put("order.items.count", messageService.getMessage("order.items.count"));
        labels.put("order.creation.date", messageService.getMessage("order.creation.date"));
        labels.put("order.delivery.date", messageService.getMessage("order.delivery.date"));
        return labels;
    }

    /**
     * Récupère les messages d'erreur traduits
     */
    public Map<String, String> getErrorMessages() {
        Map<String, String> messages = new HashMap<>();
        messages.put("error.not.found", messageService.getMessage("product.not.found"));
        messages.put("error.unauthorized", messageService.getMessage("error.unauthorized"));
        messages.put("error.forbidden", messageService.getMessage("error.forbidden"));
        messages.put("error.bad.request", messageService.getMessage("error.bad.request"));
        messages.put("error.internal", messageService.getMessage("error.internal.server"));
        return messages;
    }

    /**
     * Récupère les messages de succès traduits
     */
    public Map<String, String> getSuccessMessages() {
        Map<String, String> messages = new HashMap<>();
        messages.put("success.created", messageService.getMessage("success.item.created"));
        messages.put("success.updated", messageService.getMessage("success.item.updated"));
        messages.put("success.deleted", messageService.getMessage("success.item.deleted"));
        messages.put("success.saved", messageService.getMessage("success.item.saved"));
        return messages;
    }
}
