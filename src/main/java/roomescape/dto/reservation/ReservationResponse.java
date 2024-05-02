package roomescape.dto.reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import roomescape.domain.reservation.Reservation;
import roomescape.dto.reservationtime.ReservationTimeResponse;
import roomescape.dto.theme.ThemeResponse;

public record ReservationResponse(long id, String name, ThemeResponse theme, LocalDate date,
                                  ReservationTimeResponse time) {
    private ReservationResponse(long id, String name, ThemeResponse theme, LocalDate date, long timeId,
                                LocalTime time) {
        this(id, name, theme, date, new ReservationTimeResponse(timeId, time));
    }

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getName(),
                ThemeResponse.from(reservation.getTheme()),
                reservation.getDate(),
                new ReservationTimeResponse(reservation.getTimeId(), reservation.getTime()));
    }
}
