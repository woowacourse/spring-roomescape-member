package roomescape.reservation.dto;

import java.time.LocalDate;
import roomescape.member.domain.Member;
import roomescape.member.dto.CompletedReservation;
import roomescape.member.dto.MemberResponse;
import roomescape.reservation.domain.Reservation;

public record ReservationResponse(
        long id,
        LocalDate date,
        MemberResponse member,
        ReservationTimeResponse time,
        ThemeResponse theme
) {

    public static ReservationResponse from(CompletedReservation completedReservation) {
        return new ReservationResponse(
                completedReservation.id(),
                completedReservation.date(),
                MemberResponse.from(completedReservation.member()),
                ReservationTimeResponse.from(completedReservation.time()),
                ThemeResponse.from(completedReservation.theme())
        );
    }

    public static ReservationResponse of(Reservation reservation, Member member) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getDate(),
                MemberResponse.from(member),
                ReservationTimeResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme())
        );
    }
}
