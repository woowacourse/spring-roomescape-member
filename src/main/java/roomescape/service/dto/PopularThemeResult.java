package roomescape.service.dto;

public class PopularThemeResult {

    private final ThemeResult theme;
    private final long reservationCount;

    public PopularThemeResult(ThemeResult theme, long reservationCount) {
        this.theme = theme;
        this.reservationCount = reservationCount;
    }

    public ThemeResult getTheme() {
        return theme;
    }

    public long getReservationCount() {
        return reservationCount;
    }
}
