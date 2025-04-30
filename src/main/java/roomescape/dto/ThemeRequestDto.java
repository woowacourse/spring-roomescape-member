package roomescape.dto;

import roomescape.domain_entity.Theme;

public record ThemeRequestDto(String name, String description, String thumbnail) {
    public Theme toTheme() {
        return new Theme(name, description, thumbnail);
    }
}
