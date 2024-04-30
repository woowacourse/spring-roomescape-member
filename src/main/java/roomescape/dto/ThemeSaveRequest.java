package roomescape.dto;

import roomescape.model.Theme;

public record ThemeSaveRequest(
        String name,
        String description,
        String thumbnail
) {

    public Theme toTheme() {
        return new Theme(name, description, thumbnail);
    }
}
