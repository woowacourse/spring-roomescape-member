package roomescape.controller.theme.dto;

import jakarta.validation.constraints.NotBlank;
import roomescape.model.Theme;

public record ThemeRequestDto(
        @NotBlank(message = "테마명은 null이거나 공백일 수 없습니다")
        String name,

        @NotBlank(message = "테마 설명은 null이거나 공백일 수 없습니다")
        String description,

        @NotBlank(message = "썸네일 이미지는 null이거나 공백일 수 없습니다")
        String thumbnail
) {
    public Theme convertToTheme() {
        return new Theme(name, description, thumbnail);
    }

}
