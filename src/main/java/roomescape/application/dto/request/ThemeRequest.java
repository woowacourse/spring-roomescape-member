package roomescape.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import roomescape.domain.Theme;
import roomescape.domain.ThemeName;

public record ThemeRequest(
        @NotBlank(message = "테마 이름을 입력해주세요.")
        String name,
        @NotBlank(message = "테마 설명을 입력해주세요.")
        String description,
        @NotBlank(message = "썸네일 URL을 입력해주세요.")
        String thumbnail) {

    public Theme toTheme() {
        return new Theme(new ThemeName(name), description, thumbnail);
    }
}
