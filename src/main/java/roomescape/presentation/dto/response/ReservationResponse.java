package roomescape.presentation.dto.response;

import java.time.LocalDate;

import roomescape.domain.model.Reservation;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        StartAtResponse time,
        ReservationThemeResponse theme
) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                StartAtResponse.from(reservation.getTime()),
                ReservationThemeResponse.from(reservation.getTheme())
        );
    }
}
