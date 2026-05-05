package roomescape.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;

public record ThemeRequestDto(
        @Nullable @Size(max = 40, message = "이름은 40자 이하여야 합니다")
        String name,
        String thumbnailUrl,
        @Nullable @Size(min = 2, max = 200, message = "설명은 최소 20자 최대 200자 까지 가능합니다.")
        String description
) {
}
