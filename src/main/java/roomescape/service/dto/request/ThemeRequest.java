package roomescape.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import roomescape.domain.Theme;

public record ThemeRequest(
        @NotBlank(message = "테마 이름을 입력해주세요.")
        @Size(min = 1, max = 255, message = "테마 이름은 1자 이상 255자 이하로 입력해주세요.")
        String name,

        @NotBlank(message = "테마 설명을 입력해주세요.")
        @Size(min = 1, max = 255, message = "테마 설명은 1자 이상 255자 이하로 입력해주세요.")
        String description,

        @NotBlank(message = "테마 썸네일을 입력해주세요.")
        @Size(min = 1, max = 255, message = "테마 썸네일은 1자 이상 255자 이하로 입력해주세요.")
        String thumbnail
) {

    public Theme toTheme() {
        return new Theme(name, description, thumbnail);
    }
}
