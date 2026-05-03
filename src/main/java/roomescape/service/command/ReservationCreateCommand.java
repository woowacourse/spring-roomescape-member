package roomescape.service.command;

import java.time.LocalDate;

public record ReservationCreateCommand(
        String name,
        LocalDate date,
        long timeId
) {
}
