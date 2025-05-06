package roomescape.reservation.dto;

import java.time.LocalDate;
import roomescape.exception.custom.reason.ResponseInvalidException;
import roomescape.reservation.Reservation;
import roomescape.reservationtime.dto.ReservationTimeResponse;
import roomescape.theme.dto.ThemeResponse;

public record ReservationResponse(
        Long id,
        LocalDate date,
        String name,
        ReservationTimeResponse time,
        ThemeResponse theme
) {
    public ReservationResponse {
        if (id == null || date == null || time == null || theme == null) {
            throw new ResponseInvalidException();
        }
    }

    public static ReservationResponse from(final Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getDate(),
                reservation.getMember().getName(),
                ReservationTimeResponse.from(reservation.getReservationTime()),
                ThemeResponse.from(reservation.getTheme())
        );
    }
}
