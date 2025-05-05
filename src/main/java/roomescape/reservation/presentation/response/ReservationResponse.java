package roomescape.reservation.presentation.response;

import java.time.LocalDate;
import roomescape.reservation.business.model.entity.Reservation;
import roomescape.theme.presentation.response.ThemeResponse;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme) {

    public static ReservationResponse of(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                ReservationTimeResponse.of(reservation.getTime()),
                ThemeResponse.of(reservation.getTheme())
        );
    }
}
