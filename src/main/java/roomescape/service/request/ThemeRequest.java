package roomescape.service.request;

import jakarta.validation.constraints.NotBlank;
import roomescape.domain.Description;
import roomescape.domain.Theme;
import roomescape.domain.ThemeName;

public record ThemeRequest(
        @NotBlank(message = "테마 이름은 필수입니다.") String name,
        @NotBlank(message = "테마 설명은 필수입니다.") String description,
        @NotBlank(message = "테마 썸네일은 필수입니다.") String thumbnail
) {

    public Theme toDomain() {
        return new Theme(
                new ThemeName(name),
                new Description(description),
                thumbnail
        );
    }
}
