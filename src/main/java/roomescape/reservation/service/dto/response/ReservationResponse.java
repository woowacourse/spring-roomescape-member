package roomescape.reservation.service.dto.response;

import roomescape.theme.service.dto.response.ReservationThemeResponse;
import roomescape.theme.entity.ReservationThemeEntity;
import roomescape.time.service.dto.response.ReservationTimeResponse;
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
