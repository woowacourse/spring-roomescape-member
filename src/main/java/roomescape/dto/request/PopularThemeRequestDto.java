package roomescape.dto.request;

import jakarta.validation.constraints.Max;

public record PopularThemeRequestDto(
        @Max(value = 15) int limit,
        @Max(value = 10) int days
) {
}
