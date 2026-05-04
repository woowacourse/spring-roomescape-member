package roomescape.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationRequest(
        @NotBlank String userName,
        @NotNull Long themeId,
        @NotNull LocalDate date,
        @NotNull Long timeId
) {

}
