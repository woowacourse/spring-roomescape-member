package roomescape.dto.theme;

import jakarta.validation.constraints.NotBlank;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeDescription;
import roomescape.domain.theme.ThemeName;
import roomescape.domain.theme.ThemeThumbnail;

public record ThemeCreateRequest(
        @NotBlank(message = "테마 이름을 입력해주세요.")
        String name,

        @NotBlank(message = "테마 설명을 입력해주세요.")
        String description,

        @NotBlank(message = "테마 썸네일 URL을 입력해주세요.")
        String thumbnail) {

    public static ThemeCreateRequest of(String name, String description, String thumbnail) {
        return new ThemeCreateRequest(name, description, thumbnail);
    }

    public Theme toDomain() {
        return new Theme(
                null,
                ThemeName.from(name),
                ThemeDescription.from(description),
                ThemeThumbnail.from(thumbnail)
        );
    }
}
