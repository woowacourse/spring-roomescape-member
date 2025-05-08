package roomescape.service.dto.request;

import roomescape.domain.ReservationTheme;

public record CreateReservationThemeServiceRequest(
        String name,
        String description,
        String thumbnail
) {

    public ReservationTheme toReservationTheme() {
        return new ReservationTheme(name, description, thumbnail);
    }
}
