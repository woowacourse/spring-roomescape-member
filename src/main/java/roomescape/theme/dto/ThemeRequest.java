package roomescape.theme.dto;

import jakarta.validation.constraints.NotBlank;

public record ThemeRequest(

        @NotBlank(message = "테마 이름은 필수입니다.")
        String name,

        String description,
        String imageUrl
) {
}
