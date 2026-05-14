package roomescape.theme.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record ThemeCreateRequest(

        @NotBlank(message = "이름은 비어 있을 수 없습니다.")
        String name,
        String description,
        String thumbnailUrl
) {
}
