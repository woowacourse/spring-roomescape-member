package roomescape.dto;

import jakarta.validation.constraints.NotBlank;

public record ThemeRequest(
        @NotBlank(message = "이름은 빈 값일 수 없습니다.") String name,
        @NotBlank(message = "설명은 빈 값일 수 없습니다.") String description,
        @NotBlank(message = "url은 빈 값일 수 없습니다.") String url) {
}
