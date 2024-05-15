package roomescape.dto.reservation;

import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.dto.member.ReservationMemberResponse;
import roomescape.dto.reservationtime.ReservationTimeResponse;
import roomescape.dto.theme.ThemeResponse;

public record ReservationResponse(
        Long id,
        String date,
        ReservationMemberResponse member,
        ReservationTimeResponse time,
        ThemeResponse theme) {

    public static ReservationResponse from(Reservation reservation) {
        ReservationDate reservationDate = reservation.getDate();
        return new ReservationResponse(
                reservation.getId(),
                reservationDate.toStringDate(),
                ReservationMemberResponse.of(reservation.getMember()),
                ReservationTimeResponse.from(reservation.getReservationTime()),
                ThemeResponse.from(reservation.getTheme())
        );
    }
}
