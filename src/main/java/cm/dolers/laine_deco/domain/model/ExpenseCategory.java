package cm.dolers.laine_deco.domain.model;

public enum ExpenseCategory {
    STOCK("Stock"),
    TRANSPORT("Transport"),
    MARKETING("Marketing"),
    OTHER("Autre");

    private final String label;

    ExpenseCategory(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
