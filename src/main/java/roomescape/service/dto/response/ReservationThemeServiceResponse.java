package roomescape.service.dto.response;

import roomescape.domain.ReservationTheme;

public record ReservationThemeServiceResponse(
        Long id,
        String name,
        String description,
        String thumbnail
) {

    public static ReservationThemeServiceResponse from(ReservationTheme reservationTheme) {
        return new ReservationThemeServiceResponse(reservationTheme.id(), reservationTheme.name(),
                reservationTheme.description(), reservationTheme.thumbnail());
    }
}
