package roomescape.dto;

import roomescape.domain.Theme;

public record ThemeCreateRequest(String name, String description, String thumbnail) {

    public Theme toTheme() {
        return new Theme(name, description, thumbnail);
    }
}
