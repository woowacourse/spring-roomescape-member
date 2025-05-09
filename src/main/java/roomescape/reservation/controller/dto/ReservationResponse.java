package roomescape.reservation.controller.dto;

import java.time.LocalDate;
import roomescape.member.controller.dto.LoginMemberResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.controller.dto.RoomThemeResponse;
import roomescape.reservationtime.controller.dto.ReservationTimeResponse;

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
