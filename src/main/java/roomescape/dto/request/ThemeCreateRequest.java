package roomescape.dto.request;

import roomescape.domain.Theme;

public record ThemeCreateRequest(String name, String description, String thumbnail) {
    public Theme toTheme() {
        return new Theme(null, name, description, thumbnail);
    }
}
