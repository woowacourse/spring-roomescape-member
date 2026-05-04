package roomescape.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.theme.Theme;
import roomescape.time.ReservationTime;

public record ReservationRequest(
        @NotBlank String userName,
        @NotNull Long themeId,
        @NotNull LocalDate date,
        @NotNull Long timeId
) {

}
