package cm.dolers.laine_deco.domain.model;

public enum CartStatus {
    ABANDONED("Abandonné"),
    RECOVERED("Récupéré"),
    REMINDED("Relancé");

    private final String label;

    CartStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
