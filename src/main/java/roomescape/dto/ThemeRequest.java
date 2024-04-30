package roomescape.dto;

import roomescape.domain.Theme;
import roomescape.domain.ThemeName;

public record ThemeRequest(String name, String description, String thumbnail) {

    public Theme toTheme() {
        return new Theme(new ThemeName(name), description, thumbnail);
    }
}
