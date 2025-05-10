package roomescape.reservation.service.dto.response;

import roomescape.theme.service.dto.response.ThemeResponse;
import roomescape.theme.entity.Theme;
import roomescape.time.service.dto.response.ReservationTimeResponse;
import roomescape.reservation.entity.Reservation;

import java.time.LocalDate;

public record ReservationResponse(
        Long id,
        String memberName,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {
    public static ReservationResponse of(Reservation reservation, Theme theme, String name) {
        return new ReservationResponse(
                reservation.getId(),
                name,
                reservation.getDate(),
                ReservationTimeResponse.of(reservation.getTime()),
                ThemeResponse.of(theme)
        );
    }
}
