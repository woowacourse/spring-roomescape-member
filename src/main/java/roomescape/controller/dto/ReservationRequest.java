package roomescape.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record ReservationRequest(

        @NotBlank
        @Size(max = 255)
        String name,

        @NotNull
        LocalDate date,

        @NotNull
        Long timeId,

        @NotNull
        Long themeId
) {
}
