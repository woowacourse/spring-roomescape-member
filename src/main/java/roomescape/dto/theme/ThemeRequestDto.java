package roomescape.dto.theme;

import jakarta.validation.constraints.NotNull;
import roomescape.domain.vo.ThemeImageUrl;
import roomescape.domain.vo.ThemeName;

public record ThemeRequestDto(
        @NotNull(message = "이름은 필수 입력값입니다.")
        ThemeName name,

        String description,

        @NotNull(message = "url은 필수 입력값입니다.")
        ThemeImageUrl imageUrl
) {
}
