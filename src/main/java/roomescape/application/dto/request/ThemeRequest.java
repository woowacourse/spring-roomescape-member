package roomescape.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import roomescape.domain.Theme;
import roomescape.domain.ThemeName;

public record ThemeRequest(
        @NotBlank(message = "테마 이름을 입력해주세요.")
        String name,
        @NotBlank(message = "테마 설명을 입력해주세요.")
        @Length(max = 200, message = "테마 설명은 {max}자 이하여야 합니다.")
        String description,
        @Length(max = 200, message = "썸네일 URL은 {max}자 이하여야 합니다.")
        @NotBlank(message = "썸네일 URL을 입력해주세요.")
        String thumbnail) {

    public Theme toTheme() {
        return new Theme(new ThemeName(name), description, thumbnail);
    }
}
