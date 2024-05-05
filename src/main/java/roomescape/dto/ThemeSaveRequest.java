package roomescape.dto;

import jakarta.validation.constraints.NotBlank;
import roomescape.model.Theme;

public record ThemeSaveRequest(
        @NotBlank(message = "테마 이름이 비어 있습니다.")
        String name,

        @NotBlank(message = "테마 설명이 비어 있습니다.")
        String description,

        @NotBlank(message = "테마 썸네일이 비어 있습니다.")
        String thumbnail
) {

    public Theme toTheme() {
        return new Theme(name, description, thumbnail);
    }
}
