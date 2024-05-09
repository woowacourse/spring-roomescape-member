package roomescape.theme.dto;

import jakarta.validation.constraints.NotBlank;
import roomescape.theme.domain.Theme;

public record ThemeCreateRequest(
        @NotBlank(message = "테마 이름은 비어있을 수 없습니다.") String name,
        @NotBlank(message = "테마 설명은 비어있을 수 없습니다.") String description,
        @NotBlank(message = "테마 썸네일은 비어있을 수 없습니다.") String thumbnail) {

    public Theme toTheme() {
        return new Theme(name, description, thumbnail);
    }
}
