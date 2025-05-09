package roomescape.reservation.controller.dto;

import java.time.LocalDate;
import roomescape.member.controller.dto.MemberResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.controller.dto.ReservationTimeResponse;
import roomescape.theme.controller.dto.RoomThemeResponse;

public record ReservationResponse(long id,
                                  LocalDate date,
                                  ReservationTimeResponse time,
                                  RoomThemeResponse theme,
                                  MemberResponse member) {

    public static ReservationResponse from(final Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime()),
                RoomThemeResponse.from(reservation.getTheme()),
                MemberResponse.from(reservation.getMember())
        );
    }
}
