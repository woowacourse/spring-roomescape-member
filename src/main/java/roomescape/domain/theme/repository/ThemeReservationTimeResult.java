package roomescape.domain.theme.repository;

import java.time.LocalTime;

public record ThemeReservationTimeResult(
        Long id,
        LocalTime startAt,
        boolean isAvailable
) {
}
