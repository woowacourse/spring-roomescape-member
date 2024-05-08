package roomescape.dto.response;

import roomescape.domain.Theme;
import roomescape.domain.ThemeName;

public record ThemeResponse(Long id, String name, String description, String thumbnail) {

    public ThemeResponse(Theme theme) {
        this(
                theme.getId(),
                theme.getName().name(),
                theme.getDescription(),
                theme.getThumbnail()
        );
    }

    public Theme toTheme() {
        return new Theme(id, new ThemeName(name), description, thumbnail);
    }
}
