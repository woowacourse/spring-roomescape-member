package roomescape.dto.theme;

import jakarta.validation.constraints.NotBlank;

public record ThemeRequest(
    @NotBlank(message = "이름은 필수 입력값입니다.")
    String name,

    String description,

    @NotBlank(message = "url은 필수 입력값입니다.")
    String imageUrl
) {
}
