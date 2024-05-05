package roomescape.dto.request;

import roomescape.domain.Name;
import roomescape.domain.Theme;

public record ThemeAddRequest(String name, String description, String thumbnail) {

    public Theme toTheme() {
        return new Theme(null, new Name(name), description, thumbnail);
    }
}
