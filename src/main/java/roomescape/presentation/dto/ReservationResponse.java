package roomescape.presentation.dto;

import java.time.LocalDate;
import roomescape.business.domain.Reservation;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        PlayTimeResponse time,
        ThemeResponse theme
) {

    public static ReservationResponse from(final Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                PlayTimeResponse.from(reservation.getPlayTime()),
                ThemeResponse.from(reservation.getTheme())
        );
    }
}
