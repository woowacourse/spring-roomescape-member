package roomescape.theme.domain;

public enum SortColumn {
    RESERVATION_COUNT("reservationCount"),
    ID("id"),
    NAME("name");

    private final String value;

    SortColumn(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SortColumn fromString(String input) {
        for (SortColumn column : values()) {
            if (column.value.equalsIgnoreCase(input)) {
                return column;
            }
        }
        throw new IllegalArgumentException("지원하지 않는 정렬 기준입니다: " + input);
    }
}
