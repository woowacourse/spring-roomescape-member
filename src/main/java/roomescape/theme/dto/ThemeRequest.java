package roomescape.theme.dto;

import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeName;

public record ThemeRequest(String name, String description, String thumbnail) {

    public Theme toTheme() {
        return new Theme(null, new ThemeName(name), description, thumbnail);
    }
}
