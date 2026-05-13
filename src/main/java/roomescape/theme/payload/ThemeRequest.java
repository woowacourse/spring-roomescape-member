package roomescape.theme.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record ThemeRequest(
        @NotBlank(message = "테마 이름은 필수입니다.")
        @Size(min = 1, max = 30, message = "테마 이름은 1에서 30자 사이입니다.")
        String name,
        @NotBlank(message = "테마 설명은 필수입니다.")
        @Size(min = 1, max = 255, message = "테마 설명은 1에서 255자 사이입니다.")
        String description,
        @NotBlank(message = "썸네일 Url은 필수입니다.")
        @URL(message = "썸네일 URL 형식이 올바르지 않습니다.")
        String thumbnailUrl
) {
}
