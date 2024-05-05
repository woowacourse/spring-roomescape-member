package roomescape.dto.theme;

import java.util.Objects;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeDescription;
import roomescape.domain.theme.ThemeName;
import roomescape.domain.theme.ThemeThumbnail;

public record ThemeCreateRequest(String name, String description, String thumbnail) {

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
