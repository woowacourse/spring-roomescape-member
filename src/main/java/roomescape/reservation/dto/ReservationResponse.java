package roomescape.reservation.dto;

import roomescape.theme.dto.ReservationThemeResponse;
import roomescape.theme.entity.ReservationThemeEntity;
import roomescape.time.dto.ReservationTimeResponse;
import roomescape.reservation.entity.ReservationEntity;

import java.time.LocalDate;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        ReservationTimeResponse time,
        ReservationThemeResponse theme
) {
    public static ReservationResponse from(ReservationEntity reservation, ReservationThemeEntity theme) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime()),
                ReservationThemeResponse.from(theme)
        );
    }
}
