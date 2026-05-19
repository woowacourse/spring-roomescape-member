package roomescape.command;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReservationEditCommand(
        @NotNull LocalDate date,
        @NotNull Long timeId) {
}
