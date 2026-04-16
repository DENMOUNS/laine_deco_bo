package cm.dolers.laine_deco.domain.model;

public enum LegalPageType {
    TERMS_OF_SERVICE("CGV"),
    PRIVACY_POLICY("Politique de confidentialité"),
    LEGAL_NOTICE("Mentions légales"),
    ABOUT_US("À propos de nous"),
    SITE_VERSION("Version du site");

    private final String label;

    LegalPageType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
