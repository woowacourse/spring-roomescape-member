package roomescape.reservation.ui.dto;

import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.ui.dto.CreateThemeResponse;

public record CreateReservationResponse(
        Long id,
        LocalDate date,
        CreateReservationTimeResponse time,
        CreateThemeResponse theme
) {

    public static CreateReservationResponse from(final Reservation reservation) {
        return new CreateReservationResponse(
                reservation.getId(),
                reservation.getDate(),
                CreateReservationTimeResponse.from(reservation.getTime()),
                CreateThemeResponse.from(reservation.getTheme()));
    }
}
