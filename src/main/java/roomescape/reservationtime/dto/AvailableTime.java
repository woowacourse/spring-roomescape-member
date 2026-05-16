package roomescape.reservationtime.dto;

import java.time.LocalTime;

public record AvailableTime(
        long timeId,
        LocalTime startAt,
        boolean isAvailable
) {
}
