package cm.dolers.laine_deco.domain.model;

public enum PackType {
    BUNDLE("Bundle"),
    FLASH_PACK("Flash Pack"),
    SEASONAL_PACK("Seasonal Pack"),
    STARTER_PACK("Starter Pack");

    private final String label;

    PackType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
