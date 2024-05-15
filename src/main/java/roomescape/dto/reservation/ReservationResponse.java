package roomescape.dto.reservation;

import java.time.LocalDate;
import roomescape.domain.reservation.Reservation;
import roomescape.dto.member.MemberResponse;
import roomescape.dto.reservationtime.ReservationTimeResponse;
import roomescape.dto.theme.ThemeResponse;

public record ReservationResponse(
        long id,
        MemberResponse member,
        ThemeResponse theme,
        LocalDate date,
        ReservationTimeResponse time
) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                MemberResponse.from(reservation.getMember()),
                ThemeResponse.from(reservation.getTheme()),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime())
        );
    }
}
