package roomescape.theme.dto;

import roomescape.theme.domain.Theme;

public record ThemeRequestDto(String name, String description, String thumbnail) {
    public Theme toTheme() {
        return new Theme(null, name, description, thumbnail);
    }
}
