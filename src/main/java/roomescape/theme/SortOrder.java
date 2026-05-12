package roomescape.theme;

public enum SortOrder {
    ASC, DESC;

    public static SortOrder from(String value) {
        try {
            return SortOrder.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 정렬 방향입니다: " + value);
        }
    }
}
