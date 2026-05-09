package roomescape.theme.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ThemeSaveDto(
        @NotBlank(message = "테마명은 필수입니다.")
        String name,

        @NotBlank(message = "테마 설명은 필수입니다.")
        String description,

        String thumbnailUrl
) {
}
