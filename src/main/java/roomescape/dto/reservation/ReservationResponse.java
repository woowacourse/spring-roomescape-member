package roomescape.dto.reservation;

import java.util.Objects;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.dto.reservationtime.ReservationTimeResponse;
import roomescape.dto.theme.ThemeResponse;

public record ReservationResponse(Long id, String name, String date, ReservationTimeResponse time, ThemeResponse theme) {

    public static ReservationResponse from(Reservation reservation) {
        ReservationDate reservationDate = reservation.getDate();
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName().getValue(),
                reservationDate.toStringDate(),
                ReservationTimeResponse.from(reservation.getReservationTime()),
                ThemeResponse.from(reservation.getTheme())
        );
    }

    public static ReservationResponse of(Long id,
                                         String name,
                                         String date,
                                         ReservationTimeResponse timeResponse,
                                         ThemeResponse themeResponse) {
        return new ReservationResponse(id, name, date, timeResponse, themeResponse);
    }
}
