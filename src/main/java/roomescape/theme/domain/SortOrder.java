package roomescape.theme.domain;

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

    public static SortOrder fromString(String input) {
        for (SortOrder order : values()) {
            if (order.value.equalsIgnoreCase(input)) {
                return order;
            }
        }
        throw new IllegalArgumentException("지원하지 않는 정렬 순서입니다: " + input);
    }
}

