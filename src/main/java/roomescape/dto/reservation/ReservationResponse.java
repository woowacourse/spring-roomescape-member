package roomescape.dto.reservation;

import java.time.LocalDate;
import roomescape.domain.reservation.Reservation;
import roomescape.dto.member.MemberResponse;

public record ReservationResponse(
        long id,
        MemberResponse member,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {

    public static ReservationResponse from(final Reservation reservation) {
        return new ReservationResponse(reservation.getId(),
                MemberResponse.from(reservation.getMember()),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme())
        );
    }
}
