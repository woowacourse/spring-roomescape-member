package roomescape.theme.dto.request;

import roomescape.theme.model.Theme;

public record CreateThemeRequest(String name, String description, String thumbnail) {
    public Theme toTheme() {
        return new Theme(null, name, description, thumbnail);
    }
}
