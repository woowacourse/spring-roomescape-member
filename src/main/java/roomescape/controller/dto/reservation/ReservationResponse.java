package roomescape.controller.dto.reservation;

import java.time.LocalDate;
import roomescape.controller.dto.reservationtime.ReservationTimeResponse;
import roomescape.controller.dto.theme.ThemeResponse;
import roomescape.domain.Reservation;
import roomescape.service.dto.ReservationResult;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme())
        );
    }

    public static ReservationResponse from(ReservationResult result) {
        return new ReservationResponse(
                result.id(),
                result.name(),
                result.date(),
                ReservationTimeResponse.from(result.time()),
                ThemeResponse.from(result.theme())
        );
    }
}
