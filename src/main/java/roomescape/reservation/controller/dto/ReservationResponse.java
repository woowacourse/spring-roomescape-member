package roomescape.reservation.controller.dto;

import java.time.LocalDate;
import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.dto.ReservationMember;

public record ReservationResponse(
        long id,
        String name,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme) {
    public static ReservationResponse from(long memberReservationId, Reservation reservation, Member member) {
        return new ReservationResponse(
                memberReservationId,
                member.getName(),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme())
        );
    }

    public static ReservationResponse from(ReservationMember reservationMember) {
        return new ReservationResponse(
                reservationMember.id(),
                reservationMember.member().getName(),
                reservationMember.reservation().getDate(),
                ReservationTimeResponse.from(reservationMember.reservation().getTime()),
                ThemeResponse.from(reservationMember.reservation().getTheme())
        );
    }
}
