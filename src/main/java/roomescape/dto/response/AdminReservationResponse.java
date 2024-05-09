package roomescape.dto.response;

import java.time.LocalDate;
import roomescape.domain.Reservation;

public record AdminReservationResponse(Long id,
                                       MemberResponse member,
                                       LocalDate date,
                                       ReservationTimeResponse time,
                                       ThemeResponse theme) {

    public static AdminReservationResponse from(Reservation reservation) {
        return new AdminReservationResponse(
                reservation.getId(),
                MemberResponse.from(reservation.getMember()),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme())
        );
    }
}
