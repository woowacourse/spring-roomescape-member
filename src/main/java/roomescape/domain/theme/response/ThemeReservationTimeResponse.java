package roomescape.domain.theme.response;

import java.time.LocalTime;
import roomescape.domain.reservation.entity.ReservationTime;

public record ThemeReservationTimeResponse(
        Long id,
        LocalTime startAt,
        Boolean isAvailable
) {

    public static ThemeReservationTimeResponse from(ReservationTime time, boolean isAvailable) {
        return new ThemeReservationTimeResponse(
                time.getId(),
                time.getStartAt(),
                isAvailable
        );
    }
}
