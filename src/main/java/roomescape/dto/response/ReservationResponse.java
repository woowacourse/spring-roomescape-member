package roomescape.dto.response;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

public record ReservationResponse(
        Long id,
        String name,
        String date,
        ReservationTimeResponse time,
        Long themeId
) {
    public static ReservationResponse from(Reservation reservation, ReservationTime reservationTime) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate().toString(),
                ReservationTimeResponse.from(reservationTime),
                reservation.getThemeId()
        );
    }
}
