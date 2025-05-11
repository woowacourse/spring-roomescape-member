package roomescape.presentation.dto;

import java.time.LocalDate;
import roomescape.business.domain.Reservation;

public record ReservationResponse(
        Long id,
        UserResponse user,
        LocalDate date,
        PlayTimeResponse time,
        ThemeResponse theme
) {

    public static ReservationResponse from(final Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                UserResponse.from(reservation.getUser()),
                reservation.getDate(),
                PlayTimeResponse.from(reservation.getPlayTime()),
                ThemeResponse.from(reservation.getTheme())
        );
    }

    public static ReservationResponse withId(final Reservation reservation, final Long id) {
        return new ReservationResponse(
                id,
                UserResponse.from(reservation.getUser()),
                reservation.getDate(),
                PlayTimeResponse.from(reservation.getPlayTime()),
                ThemeResponse.from(reservation.getTheme())
        );
    }
}
