package cm.dolers.laine_deco.domain.model;

public enum OrderStatus {
    PENDING("En attente"),
    PROCESSING("En traitement"),
    SHIPPED("Expédié"),
    DELIVERED("Livré"),
    CANCELLED("Annulé");

    private final String label;

    OrderStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
