package roomescape.controller.response;

import roomescape.domain.Reservation;

public record ThemeWebResponse(Long id, String name, String description, String thumbnail) {

    public static ThemeWebResponse from(Reservation newReservation) {
        return new ThemeWebResponse(
            newReservation.getTheme().getId(),
            newReservation.getTheme().getName(),
            newReservation.getTheme().getDescription(),
            newReservation.getTheme().getThumbnail()
        );
    }
}
