package cm.dolers.laine_deco.domain.model;

public enum PaymentMethod {
    CASH("Espèces"),
    BANK_TRANSFER("Virement bancaire"),
    CHECK("Chèque"),
    CREDIT_CARD("Carte bancaire"),
    MOBILEMONEY("Mobile Money"),
    OTHER("Autre");

    private final String label;

    PaymentMethod(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
