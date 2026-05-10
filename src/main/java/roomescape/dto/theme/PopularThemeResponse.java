package roomescape.dto.theme;

import roomescape.domain.theme.ReservationThemeWithCount;

public record PopularThemeResponse(long id, String name, String description, String imageUrl, long count) {
    public static PopularThemeResponse from(ReservationThemeWithCount reservationThemeWithCount) {
        return new PopularThemeResponse(reservationThemeWithCount.id(), reservationThemeWithCount.name(), reservationThemeWithCount.description(), reservationThemeWithCount.imageUrl(), reservationThemeWithCount.count());
    }
}
