package roomescape.reservation.dto.response;

import roomescape.reservationtime.dto.response.ReservationTimeResponse;
import roomescape.theme.dto.response.ThemeResponse;
import roomescape.reservation.domain.Reservation;

public record ReservationResponse(
        Long id,
        String name,
        String date,
        ThemeResponse theme,
        ReservationTimeResponse time
) {
    public static ReservationResponse fromEntity(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getUser().getName(),
                reservation.getDate().toString(),
                ThemeResponse.from(reservation.getTheme()),
                ReservationTimeResponse.fromEntity(reservation.getReservationTime())
        );
    }
}
