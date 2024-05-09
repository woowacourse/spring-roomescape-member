package roomescape.reservation.dto;

import java.time.LocalDate;
import roomescape.member.domain.Member;
import roomescape.member.dto.MemberResponse;
import roomescape.reservation.domain.Reservation;

public record ReservationResponse(
        long id,
        LocalDate date,
        MemberResponse member,
        ReservationTimeResponse time,
        ThemeResponse theme
) {

    public static ReservationResponse from(Reservation reservation, Member member) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getDate(),
                MemberResponse.from(member),
                ReservationTimeResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme())
        );
    }
}
