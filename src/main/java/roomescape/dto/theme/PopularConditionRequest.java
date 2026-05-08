package roomescape.dto.theme;

import jakarta.validation.constraints.Pattern;
import roomescape.domain.ReservationTheme.PopularThemeCondition;

public record PopularConditionRequest(
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜는 YYYY-MM-DD 형식이여야 합니다.")
        String startDate,
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜는 YYYY-MM-DD 형식이여야 합니다.")
        String endDate,
        long size
) {
    public PopularThemeCondition to() {
        return new PopularThemeCondition(startDate, endDate, size);
    }
}
