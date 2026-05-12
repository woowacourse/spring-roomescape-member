package roomescape.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationUpdateDto(
        @NotNull LocalDate date,
        @NotNull Long timeId
) {
}
