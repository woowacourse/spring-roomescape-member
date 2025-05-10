package roomescape.presentation.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import roomescape.business.domain.Reservation;

public record ReservationResponse(
        Long id,
        LocalDate date,
        PlayTimeResponse time,
        ThemeResponse theme
) {

    public static ReservationResponse from(final Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getDate(),
                PlayTimeResponse.from(reservation.getPlayTime()),
                ThemeResponse.from(reservation.getTheme())
        );
    }

    public static ReservationResponse withId(final Reservation reservation, final Long id) {
        return new ReservationResponse(
                id,
                reservation.getDate(),
                PlayTimeResponse.from(reservation.getPlayTime()),
                ThemeResponse.from(reservation.getTheme())
        );
    }
}
