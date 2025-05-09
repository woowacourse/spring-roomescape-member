package roomescape.reservation.ui.dto;

import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.ui.dto.CreateThemeResponse;

public record AdminReservationResponse(
        Long id,
        String name,
        LocalDate date,
        CreateReservationTimeResponse time,
        CreateThemeResponse theme
) {

    public static AdminReservationResponse from(final Reservation reservation) {
        return new AdminReservationResponse(
                reservation.getId(),
                reservation.getMember().getName(),
                reservation.getDate(),
                CreateReservationTimeResponse.from(reservation.getTime()),
                CreateThemeResponse.from(reservation.getTheme()));
    }
}
