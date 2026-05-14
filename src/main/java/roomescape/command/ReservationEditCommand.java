package roomescape.command;

import java.time.LocalDate;

public record ReservationEditCommand(LocalDate date, Long timeId) {
}
