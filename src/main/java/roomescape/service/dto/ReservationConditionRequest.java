package roomescape.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record ReservationConditionRequest(
        @Positive
        Long themeId,

        @Positive
        Long memberId,

        @NotNull
        LocalDate dateFrom,

        @NotNull
        LocalDate dateTo
) {
}
