package roomescape.controller.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import roomescape.controller.ValidationMessage;

public record ReservationTimeRequest(

        @NotNull(message = ValidationMessage.TIME_IS_NULL)
        LocalTime startAt
) {
}
