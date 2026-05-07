package roomescape.reservationtime.controller.dto;

import java.time.LocalTime;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.controller.dto.ThemeResponse;

public record ReservationTimeResponse(
        Long id,
        LocalTime startAt,
        ThemeResponse theme
) {

    public static ReservationTimeResponse from(final ReservationTime reservationTime) {
        return new ReservationTimeResponse(
                reservationTime.getId(),
                reservationTime.getStartAt(),
                ThemeResponse.from(reservationTime.getTheme())
        );
    }
}
