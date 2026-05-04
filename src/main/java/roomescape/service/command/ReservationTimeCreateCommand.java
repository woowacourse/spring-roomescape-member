package roomescape.service.command;

import java.time.LocalTime;

public record ReservationTimeCreateCommand(
        LocalTime startAt
) {
}
