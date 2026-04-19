package cm.dolers.laine_deco.domain.model;

public enum RMAStatus {
    RESOLVED("Résolu"),
    PENDING("En attente"),
    APPROVED("Approuvé"),
    RECEIVED("Reçu"),
    REFUNDED("Remboursé"),
    REJECTED("Rejeté");

    private final String label;

    RMAStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

