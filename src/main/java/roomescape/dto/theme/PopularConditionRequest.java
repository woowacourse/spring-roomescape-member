package roomescape.dto.theme;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import roomescape.domain.theme.PopularThemeCondition;

public record PopularConditionRequest(
        @NotNull
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜는 YYYY-MM-DD 형식이여야 합니다.")
        String startDate,
        @NotNull
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜는 YYYY-MM-DD 형식이여야 합니다.")
        String endDate,
        @NotNull
        Long size
) {
    public PopularThemeCondition to() {
        return new PopularThemeCondition(startDate, endDate, size);
    }
}
