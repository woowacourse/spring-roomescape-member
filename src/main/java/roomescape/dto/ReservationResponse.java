package roomescape.dto;

import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record ReservationResponse(Long id,
                                  LocalDate date,
                                  ReservationTimeResponse reservationTime,
                                  ThemeResponse theme) {

    public static ReservationResponse from(Reservation reservation) {
        ReservationTime reservationTime = reservation.getTime();
        Theme theme = reservation.getTheme();
        return new ReservationResponse(
                reservation.getId(),
                reservation.getDate(),
                ReservationTimeResponse.from(reservationTime),
                ThemeResponse.from(theme));
    }
}
