package cm.dolers.laine_deco.domain.model;

public enum ExpenseStatus {
    PENDING("En attente"),
    APPROVED("Approuvée"),
    REJECTED("Rejetée");

    private final String label;

    ExpenseStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
