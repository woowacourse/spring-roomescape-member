package roomescape.reservation.ui.dto;

import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.ui.dto.CreateThemeResponse;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        CreateReservationTimeResponse time,
        CreateThemeResponse theme
) {

    public static ReservationResponse from(final Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                CreateReservationTimeResponse.from(reservation.getTime()),
                CreateThemeResponse.from(reservation.getTheme()));
    }
}
