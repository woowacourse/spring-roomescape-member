package roomescape.reservation.presentation.dto;

import roomescape.reservation.domain.Reservation;
import roomescape.reservationTime.presentation.dto.ReservationTimeResponse;
import roomescape.theme.presentation.dto.ThemeResponse;

public record ReservationResponse(
        Long id,
        String name,
        String date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate().toString(),
                ReservationTimeResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme())
        );
    }
}
