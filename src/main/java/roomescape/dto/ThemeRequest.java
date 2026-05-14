package roomescape.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ThemeRequest(
        @NotBlank(message = "테마 이름은 빈값일 수 없습니다.")
        @Size(max = 20, message = "테마 이름은 20자 이하여야 합니다.")
        String name,

        @NotBlank(message = "테마 설명은 빈값일 수 없습니다.")
        @Size(max = 1000, message = "테마 설명은 1000자 이하여야 합니다.")
        String description,

        @NotBlank(message = "테마 URL은 빈값일 수 없습니다.")
        String url) {
}
