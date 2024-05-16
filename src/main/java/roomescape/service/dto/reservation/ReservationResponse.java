package roomescape.service.dto.reservation;

import roomescape.domain.reservation.Reservation;
import roomescape.service.dto.login.MemberNameResponse;

import java.time.LocalDate;

public record ReservationResponse(Long id, MemberNameResponse member, LocalDate date,
                                  ReservationTimeResponse time,
                                  ThemeResponse theme) {

    public static ReservationResponse of(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                MemberNameResponse.of(reservation.getMember()),
                reservation.getDate(),
                ReservationTimeResponse.of(reservation.getReservationTime()),
                ThemeResponse.of(reservation.getTheme()));
    }
}
