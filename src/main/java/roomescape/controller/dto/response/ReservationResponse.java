package roomescape.controller.dto.response;

import java.time.LocalDate;
import roomescape.domain.reservation.Reservation;

public record ReservationResponse(long id,
                                  LocalDate date,
                                  ReservationTimeResponse time,
                                  RoomThemeResponse theme,
                                  LoginMemberResponse member) {

    public static ReservationResponse from(final Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime()),
                RoomThemeResponse.from(reservation.getTheme()),
                LoginMemberResponse.from(reservation.getMember())
        );
    }
}
