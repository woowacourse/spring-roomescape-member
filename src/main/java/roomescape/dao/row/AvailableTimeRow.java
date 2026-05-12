package roomescape.dao.row;

import java.time.LocalTime;

public record AvailableTimeRow(
        Long id,
        LocalTime startAt,
        boolean alreadyBooked
) {
}
