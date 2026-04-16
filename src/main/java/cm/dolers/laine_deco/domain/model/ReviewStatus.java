package cm.dolers.laine_deco.domain.model;

public enum ReviewStatus {
    PENDING("En attente"),
    APPROVED("Approuvé"),
    REJECTED("Rejeté");

    private final String label;

    ReviewStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
