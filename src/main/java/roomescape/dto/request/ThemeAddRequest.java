package roomescape.dto.request;

import roomescape.domain.Theme;
import roomescape.domain.ThemeName;

public record ThemeAddRequest(String name, String description, String thumbnail) {

    public Theme toTheme() {
        return new Theme(null, new ThemeName(name), description, thumbnail);
    }
}
