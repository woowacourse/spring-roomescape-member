package roomescape.theme;

public enum ThemeSort {
    RESERVATION_COUNT("reservationCount"),
    ID("id"),
    NAME("name");

    private final String column;

    ThemeSort(String column) {
        this.column = column;
    }

    public String getColumn() {
        return column;
    }

    public static ThemeSort from(String value) {
        for (ThemeSort sort : values()) {
            if (sort.name().equalsIgnoreCase(value) || sort.column.equalsIgnoreCase(value)) {
                return sort;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 정렬 기준입니다: " + value);
    }
}
