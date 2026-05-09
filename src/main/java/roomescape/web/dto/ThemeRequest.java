package roomescape.web.dto;

import jakarta.validation.constraints.NotBlank;

public record ThemeRequest(
        @NotBlank(message = "이름은 필수 값입니다.")
        String name,

        @NotBlank(message = "설명은 필수 값입니다.")
        String description,

        @NotBlank(message = "썸네일 이미지는 필수 값입니다.")
        String thumbnailImageUrl
) {
}
