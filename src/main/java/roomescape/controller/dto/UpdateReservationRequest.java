package roomescape.controller.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.controller.ValidationMessage;

public record UpdateReservationRequest(
        @NotNull(message = ValidationMessage.DATE_IS_NULL)
        LocalDate date,

        @NotNull(message = ValidationMessage.TIME_ID_IS_NULL)
        Long timeId
) {
}
