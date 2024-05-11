package roomescape.ui.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import roomescape.application.dto.request.ThemeCreationRequest;

public record ThemeRequest(
        @NotBlank(message = "테마명은 필수입니다.")
        String name,

        @NotBlank(message = "테마 설명은 필수입니다.")
        String description,

        @NotBlank(message = "썸네일 URL은 필수입니다.")
        @Pattern(regexp = "^https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)$",
                message = "URL 형식이 올바르지 않습니다.")
        String thumbnail
) {

    public ThemeCreationRequest toThemeCreationRequest() {
        return new ThemeCreationRequest(name, description, thumbnail);
    }
}
