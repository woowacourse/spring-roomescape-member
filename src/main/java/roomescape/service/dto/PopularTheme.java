package roomescape.service.dto;

import roomescape.repository.ThemeEntity;


public class PopularTheme {
    private final ThemeEntity themeEntity;
    private final long reservationCount;

    public PopularTheme(ThemeEntity themeEntity, long reservationCount) {
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
