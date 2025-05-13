package roomescape.application.dto;

import java.time.LocalDate;
import roomescape.domain.Reservation;

public record ReservationResponse(
        Long id,
        LocalDate date,
        ReservationTimeResponse reservationTime,
        ThemeResponse theme,
        MemberResponse member
) {

    public ReservationResponse(Reservation reservation) {
        this(
                reservation.getId(),
                reservation.getDate(),
                new ReservationTimeResponse(reservation.getReservationTime()),
                new ThemeResponse(reservation.getTheme()),
                new MemberResponse(reservation.getMember())
        );
    }
}
