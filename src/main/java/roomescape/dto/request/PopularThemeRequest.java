package roomescape.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.Period;

public record PopularThemeRequest(
        @NotNull(message = "startDate를 입력해주세요.")
        LocalDate startDate,

        @NotNull(message = "endDate를 입력해주세요.")
        LocalDate endDate,

        @Positive(message = "limit는 양수여야 합니다.")
        @Max(value = 50, message = "limit는 50보다 이하여야 합니다.")
        Integer limit
) {

    public PopularThemeRequest {
        if (limit == null) {
            limit = 10;
        }
    }

    public void validatePeriod() {
        Period period = Period.between(startDate, endDate);
        if (period.getDays() > 14) {
            throw new IllegalArgumentException("최대 조회 가능 기간은 2주(14일) 입니다.");
        }
    }
}
