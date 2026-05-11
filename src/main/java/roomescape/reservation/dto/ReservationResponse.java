package roomescape.reservation.dto;

import java.time.LocalDate;
import roomescape.reservation.Reservation;
import roomescape.theme.dto.ThemeResponse;
import roomescape.time.dto.ReservationTimeResponse;

public record ReservationResponse(long id, String userName, ThemeResponse theme, LocalDate date,
                                  ReservationTimeResponse time) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getUserName(),
                ThemeResponse.from(reservation.getTheme()),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime())
        );
    }
}
