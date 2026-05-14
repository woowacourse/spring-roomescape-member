package roomescape.theme.service;

import java.time.LocalTime;

public record TimeAvailability(
        Long id,
        LocalTime startAt,
        Boolean isAvailable
) {
    public static TimeAvailability of(Long id, LocalTime startAt, Boolean isAvailable) {
        return new TimeAvailability(id, startAt, isAvailable);
    }
}
