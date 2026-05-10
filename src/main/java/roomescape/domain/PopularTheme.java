package roomescape.domain;

public class PopularTheme {

    private final Theme theme;
    private final long reservationCount;

    public PopularTheme(Theme theme, long reservationCount) {
        this.theme = theme;
        this.reservationCount = reservationCount;
    }

    public Theme getTheme() {
        return theme;
    }

    public long getReservationCount() {
        return reservationCount;
    }
}
