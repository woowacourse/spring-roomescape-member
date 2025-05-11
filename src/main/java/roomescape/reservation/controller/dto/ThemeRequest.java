package roomescape.reservation.controller.dto;

import jakarta.validation.constraints.NotBlank;
import roomescape.reservation.domain.Theme;

public record ThemeRequest(
        @NotBlank(message = "테마 이름을 입력해주세요.")
        String name,
        @NotBlank(message = "테마 설명을 입력해주세요.")
        String description,
        @NotBlank(message = "대표 이미지를 입력해주세요.")
        String thumbnail
) {

    public Theme toThemeWithoutId() {
        return new Theme(null, name, description, thumbnail);
    }

}
