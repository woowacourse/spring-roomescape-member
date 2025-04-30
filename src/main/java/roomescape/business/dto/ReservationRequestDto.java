package roomescape.business.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationRequestDto(
        @NotEmpty String name,
        @NotNull LocalDate date,
        @NotNull Long timeId,
        @NotNull Long themeId
) {
}
