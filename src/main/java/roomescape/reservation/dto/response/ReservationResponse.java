package roomescape.reservation.dto.response;

import java.time.LocalDate;
import roomescape.member.dto.response.MemberResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.dto.response.ReservationTimeResponse;
import roomescape.theme.dto.response.ThemeResponse;

public record ReservationResponse(
        long id,
        LocalDate date,
        MemberResponse member,
        ReservationTimeResponse time,
        ThemeResponse theme
) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getDate(),
                MemberResponse.from(reservation.getMember()),
                ReservationTimeResponse.from(reservation.getTime()), ThemeResponse.from(reservation.getTheme())
        );
    }
}
