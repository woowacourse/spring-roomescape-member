package roomescape.dto.theme;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import roomescape.domain.theme.PopularThemeCondition;

import java.time.LocalDate;

public record PopularConditionRequest(
        @NotNull
        LocalDate startDate,
        @NotNull
        LocalDate endDate,
        @NotNull
        Long size
) {
    public PopularThemeCondition to() {
        return new PopularThemeCondition(startDate, endDate, size);
    }
}
