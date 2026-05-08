package roomescape.theme.repository;

public enum SortOrder {
    ASC("ASC"),
    DESC("DESC");

    private final String value;

    SortOrder(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SortOrder fromString(String order) {
        try {
            return SortOrder.valueOf(order.toUpperCase());
        } catch (IllegalArgumentException e) {
            return DESC; // 기본값은 DESC
        }
    }
}

