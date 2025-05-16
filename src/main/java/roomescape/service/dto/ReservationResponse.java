package roomescape.service.dto;

import java.time.LocalDate;
import roomescape.domain.Reservation;

public record ReservationResponse(
        long id,
        String name,
        LocalDate date,
        ReservationTimeResponse time,
        ReservationThemeResponse theme) {

    public static ReservationResponse fromV2(Reservation reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getMemberName(), reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime()),
                ReservationThemeResponse.from(reservation.getTheme()));
    }
}
