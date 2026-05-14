package roomescape.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import roomescape.command.ReservationSaveCommand;

import java.time.LocalDate;

public record ReservationRequest(
        @NotBlank @Size(max = 255) String name,
        @NotNull LocalDate date,
        @NotNull Long timeId,
        @NotNull Long themeId) {

    public ReservationSaveCommand toSaveCommand() {
        return new ReservationSaveCommand(name, date, timeId, themeId);
    }
}
