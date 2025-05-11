package roomescape.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record SearchConditionRequest(

        @NotNull
        Long themeId,

        @NotNull
        Long memberId,

        @NotNull
        LocalDate dateFrom,

        @NotNull
        LocalDate dateTo
) {
}
