package roomescape.request;

import jakarta.validation.constraints.NotNull;
import roomescape.command.ReservationEditCommand;

import java.time.LocalDate;

public record ReservationEditRequest(
        @NotNull LocalDate date,
        @NotNull Long timeId) {
    public ReservationEditCommand toCommand() {
        return new ReservationEditCommand(date, timeId);
    }
}
