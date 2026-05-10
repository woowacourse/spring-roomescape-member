package roomescape.domain.theme.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record ThemeCreateRequest(
        @NotBlank(message = "name은 공백일 수 없습니다.")
        @Length(min = 3, max = 20, message = "name은 3~20자의 문자열만 가능합니다.")
        String name,

        @NotBlank(message = "description은 공백일 수 없습니다.")
        String description,

        @NotBlank(message = "thumbnailUrl은 공백일 수 없습니다.")
        String thumbnailUrl
) {
}
