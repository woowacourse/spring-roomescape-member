package roomescape.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import roomescape.domain.Theme;

public record ThemeRequest(
        @NotBlank(message = "테마 이름은 필수값 입니다.")
        String name,

        @NotBlank(message = "테마 설명은 필수값 입니다.")
        String description,

        @NotBlank(message = "썸네일은 필수값 입니다.")
        @URL(message = "썸네일은 URL 형식이어야 합니다.")
        String thumbnail
) {
    public Theme toTheme() {
        return Theme.createWithoutId(
                name,
                description,
                thumbnail
        );
    }
}
