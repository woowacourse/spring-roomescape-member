package roomescape.dto.theme;

import roomescape.domain.ReservationTheme.ReservationThemeWithCount;

public record PopularReservationThemeResponse(long id, String name, String description, String imageUrl, long count) {
    public static PopularReservationThemeResponse from(ReservationThemeWithCount reservationThemeWithCount) {
        return new PopularReservationThemeResponse(reservationThemeWithCount.id(), reservationThemeWithCount.name(), reservationThemeWithCount.description(), reservationThemeWithCount.imageUrl(), reservationThemeWithCount.count());
    }
}
