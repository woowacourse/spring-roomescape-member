package roomescape.reservation.service.dto.response;

import roomescape.theme.service.dto.response.ThemeResponse;
import roomescape.theme.entity.ThemeEntity;
import roomescape.time.service.dto.response.ReservationTimeResponse;
import roomescape.reservation.entity.ReservationEntity;

import java.time.LocalDate;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {
    public static ReservationResponse from(ReservationEntity reservation, ThemeEntity theme) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime()),
                ThemeResponse.from(theme)
        );
    }
}
