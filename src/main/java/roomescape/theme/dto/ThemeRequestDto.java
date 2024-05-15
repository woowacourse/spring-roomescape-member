package roomescape.theme.dto;

import jakarta.validation.constraints.NotBlank;

import roomescape.theme.domain.Theme;

public record ThemeRequestDto(
        @NotBlank(message = "테마의 이름을 입력해야 합니다.") String name,
        String description,
        String thumbnail
) {
    public Theme toTheme() {
        return new Theme(name, description, thumbnail);
    }
}
