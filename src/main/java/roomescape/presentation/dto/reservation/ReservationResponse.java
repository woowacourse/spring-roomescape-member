package roomescape.presentation.dto.reservation;

import java.time.LocalDate;
import roomescape.business.domain.Reservation;
import roomescape.presentation.dto.theme.ThemeResponse;
import roomescape.presentation.dto.UserResponse;
import roomescape.presentation.dto.playtime.PlayTimeResponse;

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
