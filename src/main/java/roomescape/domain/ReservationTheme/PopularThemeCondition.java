package roomescape.domain.ReservationTheme;

public record PopularThemeCondition(String startDate, String endDate, long size) {
    private static final long DEFAULT_SIZE = 10L;

    public PopularThemeCondition {
        if(size == 0) {
            size = DEFAULT_SIZE;
        }
    }
}
