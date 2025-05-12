package roomescape.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AdminReservationRequestDto(
        @NotNull LocalDate date,
        @NotNull Long timeId,
        @NotNull Long themeId,
        @NotNull Long memberId
) {
}
