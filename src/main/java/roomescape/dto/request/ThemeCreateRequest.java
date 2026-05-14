package roomescape.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ThemeCreateRequest(
        @NotBlank(message = "테마 이름은 필수로 입력해야 합니다.")
        String name,

        @NotBlank(message = "테마 설명은 필수로 입력해야 합니다.")
        String description,

        @NotBlank(message = "테마 이미지 URL는 필수로 입력해야 합니다.")
        String imgUrl
) {
}
