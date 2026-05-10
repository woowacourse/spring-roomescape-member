package roomescape.theme.service.dto;

import java.time.LocalTime;

public record ThemeTimeAvailability(
        long id,
        LocalTime startAt,
        boolean isAvailable
) {
}
