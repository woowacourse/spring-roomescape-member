package roomescape.controller.reservation;

import roomescape.controller.login.MemberCheckResponse;
import roomescape.controller.theme.ReservationThemeResponse;
import roomescape.controller.time.TimeResponse;
import roomescape.domain.Reservation;

import java.time.format.DateTimeFormatter;

public record ReservationResponse(Long id, String date, TimeResponse time,
                                  ReservationThemeResponse theme, MemberCheckResponse member) {

    public static ReservationResponse from(final Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                TimeResponse.from(reservation.getTime(), false),
                ReservationThemeResponse.from(reservation.getTheme()),
                new MemberCheckResponse(reservation.getMember().getName())
        );
    }
}
