package roomescape.reservationtime;

import java.time.LocalTime;

public record AvailableTime(
        long timeId,
        LocalTime startAt,
        boolean isAvailable
) {
}
