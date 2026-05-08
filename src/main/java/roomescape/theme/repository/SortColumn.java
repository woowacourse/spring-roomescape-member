package roomescape.theme.repository;

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

    public static SortColumn fromString(String sort) {
        try {
            return SortColumn.valueOf(sort.toUpperCase());
        } catch (IllegalArgumentException e) {
            return RESERVATION_COUNT;
        }
    }
}
