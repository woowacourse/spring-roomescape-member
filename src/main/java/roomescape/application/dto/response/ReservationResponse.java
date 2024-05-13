package roomescape.application.dto.response;

import java.time.LocalDate;
import roomescape.domain.reservation.Reservation;

public record ReservationResponse(
        long id,
        MemberResponse member,
        ThemeResponse theme,
        LocalDate date,
        ReservationTimeResponse time
) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                MemberResponse.from(reservation.getMember()),
                ThemeResponse.from(reservation.getTheme()),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getReservationTime())
        );
    }
}
