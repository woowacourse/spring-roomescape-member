package roomescape.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ThemeRequestDto(
        @NotBlank(message = "이름은 필수입니다")
        @Size(max = 20, message = "이름은 20자 이하여야 합니다")
        String name,

        @NotBlank(message = "썸네일 URL은 필수입니다")
        @Pattern(regexp = "^https?://.+", message = "올바른 URL 형식이 아닙니다")
        String thumbnailUrl,

        @NotBlank
        @Size(min = 2, max = 200, message = "설명은 최소 2자 최대 200자 까지 가능합니다.")
        String description
) {
}
