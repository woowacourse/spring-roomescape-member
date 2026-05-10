package roomescape.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import roomescape.controller.ValidationMessage;

public record ReservationRequest(

        @NotBlank(message = ValidationMessage.NAME_IS_BLANK)
        @Size(max = 255, message = ValidationMessage.NAME_OVER_MAX_LENGTH)
        String name,

        @NotNull(message = ValidationMessage.DATE_IS_NULL)
        LocalDate date,

        @NotNull(message = ValidationMessage.TIME_ID_IS_NULL)
        Long timeId,

        @NotNull(message = ValidationMessage.THEME_ID_IS_NULL)
        Long themeId
) {
}
