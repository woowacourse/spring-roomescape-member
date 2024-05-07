package roomescape.controller.dto;

import roomescape.domain.Reservation;

public record CreateThemeResponse(Long id, String name, String description, String thumbnail) {

    public static CreateThemeResponse from(Reservation newReservation) {
        return new CreateThemeResponse(
            newReservation.getTheme().getId(),
            newReservation.getTheme().getName(),
            newReservation.getTheme().getDescription(),
            newReservation.getTheme().getThumbnail()
        );
    }
}
