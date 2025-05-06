package roomescape.controller.rest.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record CreateReservationTimeRequest(
        @NotNull LocalTime startAt
) {
}
