package roomescape.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AdminReservationSearchRequestDto(
        @NotNull Long memberId,
        @NotNull Long themeId,
        @NotNull LocalDate dateFrom,
        @NotNull LocalDate dateTo
) {
}
