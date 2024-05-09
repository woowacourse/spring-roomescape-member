package roomescape.controller.dto.response;

import java.time.LocalDate;
import roomescape.domain.roomescape.Reservation;

public record AdminReservationResponse(
        long id,
        MemberNameResponse member,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {
    public static AdminReservationResponse from(final Reservation reservation) {
        return new AdminReservationResponse(
                reservation.getId(),
                new MemberNameResponse(reservation.getName()),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme())
        );
    }
}
