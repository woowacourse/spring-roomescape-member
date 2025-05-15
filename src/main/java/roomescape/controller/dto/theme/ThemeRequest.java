package roomescape.controller.dto.theme;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ThemeRequest(
        @NotBlank @Size(max = 20, message = "테마 이름은 20자를 넘길 수 없습니다.") String name,
        @NotNull String description,
        @NotNull String thumbnail
) {
}
