package roomescape.dto.request;

import roomescape.domain.ReservationTheme;

public record CreateReservationThemeRequest(
        String name,
        String description,
        String thumbnail
) {

    public ReservationTheme toReservationTheme() {
        return new ReservationTheme(name, description, thumbnail);
    }
}
