package roomescape.theme.service;

import java.time.LocalTime;

public record AvailableTime(
        Long id,
        LocalTime time,
        Boolean isAvailable
) {
}
