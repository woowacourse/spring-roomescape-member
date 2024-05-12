package roomescape.dto.request;

import jakarta.validation.constraints.NotBlank;
import roomescape.domain.Theme;

public record ThemeRequest(
        @NotBlank(message = "name을 입력해주세요.")
        String name,

        @NotBlank(message = "description을 입력해주세요.")
        String description,

        @NotBlank(message = "thumbnail을 입력해주세요.")
        String thumbnail) {

    public Theme toTheme() {
        return new Theme(name, description, thumbnail);
    }
}
