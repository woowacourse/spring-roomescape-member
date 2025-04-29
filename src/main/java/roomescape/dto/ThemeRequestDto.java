package roomescape.dto;

import roomescape.model.Theme;

public record ThemeRequestDto(
        String name,
        String description,
        String thumbnail
) {
    public Theme convertToTheme() {
        return new Theme(name, description, thumbnail);
    }
}
