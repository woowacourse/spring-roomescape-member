package roomescape.time.presentation.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AvailableReservationTimeRequest(
        @NotNull(message = "themeId는 필수입니다.")
        Long themeId,
        @NotNull(message = "date는 필수입니다.")
        LocalDate date
) {
}
