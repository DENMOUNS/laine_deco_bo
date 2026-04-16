package cm.dolers.laine_deco.domain.model;

public enum NotificationType {
    ORDER("Commande"),
    STOCK("Stock"),
    INQUIRY("Demande"),
    CUSTOMER("Client");

    private final String label;

    NotificationType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
