package cm.dolers.laine_deco.domain.model;

/**
 * Enum pour les thèmes d'interface disponibles
 */
public enum ThemePreference {
    LIGHT("light", "Mode clair"),
    DARK("dark", "Mode sombre");

    private final String value;
    private final String label;

    ThemePreference(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Convertit une string en ThemePreference
     */
    public static ThemePreference fromString(String value) {
        for (ThemePreference theme : ThemePreference.values()) {
            if (theme.value.equalsIgnoreCase(value)) {
                return theme;
            }
        }
        return LIGHT; // Défaut
    }
}
