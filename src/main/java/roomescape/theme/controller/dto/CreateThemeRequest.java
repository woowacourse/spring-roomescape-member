package roomescape.theme.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateThemeRequest(
        @NotBlank(message = "이름은 공백일 수 없습니다. 이름 입력 란을 채워주세요.")
        String name,

        String description,

        String imageUrl
) {
}
