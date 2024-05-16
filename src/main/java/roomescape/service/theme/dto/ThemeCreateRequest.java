package roomescape.service.theme.dto;

import roomescape.domain.theme.Theme;

public record ThemeCreateRequest(String name, String description, String thumbnail) {

    public Theme toTheme() {
        return new Theme(name, description, thumbnail);
    }
}
