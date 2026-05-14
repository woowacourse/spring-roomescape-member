package roomescape.dto.request;

import jakarta.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.URL;

public record ThemeRequest(

        @NotBlank(message = "테마 이름은 필수입니다.")
        String name,

        @URL(message = "유효한 URL 형식이 아닙니다.")
        String thumbnailUrl,

        @NotBlank(message = "테마 설명은 필수입니다.")
        String description
) {
}