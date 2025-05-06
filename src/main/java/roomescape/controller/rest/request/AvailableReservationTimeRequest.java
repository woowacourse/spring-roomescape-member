package roomescape.controller.rest.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AvailableReservationTimeRequest(
        @NotNull LocalDate date,
        @NotNull Long themeId
) {
}
