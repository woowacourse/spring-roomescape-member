package roomescape.controller.reservation;

import roomescape.controller.theme.ReservationThemeResponse;
import roomescape.controller.time.TimeResponse;
import roomescape.domain.Reservation;

import java.time.format.DateTimeFormatter;

public record ReservationResponse(Long id, String name, String date, TimeResponse time,
                                  ReservationThemeResponse theme) {

    public static ReservationResponse from(final Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getMember().getName(),
                reservation.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                TimeResponse.from(reservation.getTime(), false),
                ReservationThemeResponse.from(reservation.getTheme())
        );
    }
}
