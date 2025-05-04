package roomescape.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateThemeRequest(
        @NotBlank(message = "테마명은 필수입니다")
        String name,

        @NotBlank(message = "테마 설명은 필수입니다")
        String description,

        @NotBlank(message = "테마 썸네일 URL은 필수입니다")
        String thumbnail) {
}
