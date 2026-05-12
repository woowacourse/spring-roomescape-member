package roomescape.dto.theme;

import jakarta.validation.constraints.NotBlank;
import roomescape.domain.theme.Theme;

public record AddThemeRequest(
        @NotBlank(message = "테마 이름은 반드시 포함되어야 합니다.")
        String name,
        @NotBlank(message = "테마 설명은 반드시 포함되어야 합니다.")
        String description,
        @NotBlank(message = "썸네일 이미지는 반드시 포함되어야 합니다.")
        String imageUrl
) {

    public Theme toEntity() {
        return new Theme(name, description, imageUrl);
    }
}
