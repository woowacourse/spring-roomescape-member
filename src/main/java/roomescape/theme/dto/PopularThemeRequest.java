package roomescape.theme.dto;

import jakarta.validation.constraints.Positive;

public record PopularThemeRequest(
        @Positive(message = "limit는 양수여야 합니다.")
        int limit,

        @Positive(message = "날짜는 양수여야 합니다.")
        int days
) {
    public PopularThemeRequest {
        if (limit == 0) {
            limit = 10;
        }
        if (days == 0) {
            days = 7;
        }
    }
}
