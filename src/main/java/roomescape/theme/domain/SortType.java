package roomescape.theme.domain;

public enum SortType {
    RESERVATION_COUNT("reservationCount"),
    ID("id"),
    NAME("name");

    private final String value;

    SortType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SortType fromString(String input) {
        for (SortType column : values()) {
            if (column.value.equalsIgnoreCase(input)) {
                return column;
            }
        }
        throw new IllegalArgumentException("지원하지 않는 정렬 기준입니다: " + input);
    }
}
