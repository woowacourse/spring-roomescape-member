package roomescape.dto.theme;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import roomescape.application.dto.ThemeCreationRequest;

public record ThemeRequest(
        @NotBlank(message = "테마명은 필수입니다.")
        @Size(min = 3, max = 20, message = "테마명은 3글자 이상 20글자 이하여야 합니다.")
        String name,

        @NotBlank(message = "테마 설명은 필수입니다.")
        String description,

        @NotBlank(message = "썸네일 URL은 필수입니다.")
        @Pattern(regexp = "^https?://.*\\.(png|jpe?g|gif)$", message = "URL 형식에 맞지 않습니다.")
        String thumbnail
) {

    public ThemeCreationRequest toThemeCreationRequest() {
        return new ThemeCreationRequest(name, description, thumbnail);
    }
}
