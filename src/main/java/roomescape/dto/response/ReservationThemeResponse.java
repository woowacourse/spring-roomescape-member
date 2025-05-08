package roomescape.dto.response;

import roomescape.domain.ReservationTheme;

public record ReservationThemeResponse(
        Long id,
        String name,
        String description,
        String thumbnail
) {

    public static ReservationThemeResponse from(ReservationTheme reservationTheme) {
        return new ReservationThemeResponse(reservationTheme.id(), reservationTheme.name(),
                reservationTheme.description(), reservationTheme.thumbnail());
    }
}
