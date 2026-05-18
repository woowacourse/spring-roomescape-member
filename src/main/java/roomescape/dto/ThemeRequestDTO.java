package roomescape.dto;

import jakarta.validation.constraints.NotBlank;

public record ThemeRequestDTO(
        @NotBlank(message = "테마 이름은 필수입니다.") String name,
        @NotBlank(message = "테마 설명은 필수입니다.") String description,
        @NotBlank(message = "썸네일 이미지는 필수입니다.") String imageUrl
) {
}
