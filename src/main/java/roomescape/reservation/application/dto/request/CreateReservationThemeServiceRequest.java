package roomescape.reservation.application.dto.request;

import roomescape.reservation.domain.entity.ReservationTheme;

public record CreateReservationThemeServiceRequest(
        String name,
        String description,
        String thumbnail
) {

    public ReservationTheme toReservationTheme() {
        return new ReservationTheme(name, description, thumbnail);
    }
}
