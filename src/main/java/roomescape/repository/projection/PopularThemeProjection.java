package roomescape.repository.projection;

public class PopularThemeProjection {
    private final ThemeEntity themeEntity;
    private final long reservationCount;

    public PopularThemeProjection(ThemeEntity themeEntity, long reservationCount) {
        this.themeEntity = themeEntity;
        this.reservationCount = reservationCount;
    }

    public ThemeEntity getThemeEntity() {
        return themeEntity;
    }

    public long getReservationCount() {
        return reservationCount;
    }
}
