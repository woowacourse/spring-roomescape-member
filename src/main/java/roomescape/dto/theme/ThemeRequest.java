package roomescape.dto.theme;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import roomescape.application.dto.ThemeCreationRequest;

public record ThemeRequest(
        @NotBlank(message = "테마명은 필수입니다.")
        @Size()
        String name,

        @NotBlank(message = "테마 설명은 필수입니다.")
        String description,

        @NotBlank(message = "썸네일 URL은 필수입니다.")
        String thumbnail
) {

    public ThemeCreationRequest toThemeCreationRequest() {
        return new ThemeCreationRequest(name, description, thumbnail);
    }
}
