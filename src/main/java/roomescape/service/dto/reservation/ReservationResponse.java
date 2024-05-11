package roomescape.service.dto.reservation;

import roomescape.domain.Reservation;
import roomescape.domain.Theme;

import java.time.LocalDate;
import roomescape.service.dto.member.MemberResponse;
import roomescape.service.dto.reservationtime.ReservationTimeResponse;

public record ReservationResponse(Long id, MemberResponse member, LocalDate date, ReservationTimeResponse time, Theme theme) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                MemberResponse.from(reservation.getMember()),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime()),
                reservation.getTheme()
        );
    }
}
