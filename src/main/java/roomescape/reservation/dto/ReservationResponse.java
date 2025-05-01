package roomescape.reservation.dto;

import java.time.LocalDate;
import roomescape.globalException.ResponseInvalidException;
import roomescape.reservation.Reservation;
import roomescape.reservationTime.dto.ReservationTimeResponse;
import roomescape.theme.dto.ThemeResponse;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {
    public ReservationResponse {
        if(id == null || name == null || name == null || date == null || time == null || theme == null){
            throw new ResponseInvalidException();
        }
    }

    public static ReservationResponse from(final Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getReservationTime()),
                ThemeResponse.from(reservation.getTheme())
        );
    }
}
