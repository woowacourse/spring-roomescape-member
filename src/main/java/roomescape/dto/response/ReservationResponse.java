package roomescape.dto.response;

import java.time.LocalDate;
import roomescape.domain.Reservation;

public record ReservationResponse(
    Long id,
    LocalDate date,
    StartAtResponse time,
    ReservationThemeResponse theme,
    MemberResponse member
) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
            reservation.getId(),
            reservation.getDate(),
            StartAtResponse.from(reservation.getTime()),
            ReservationThemeResponse.from(reservation.getTheme()),
            MemberResponse.from(reservation.getMember())
        );
    }
}
