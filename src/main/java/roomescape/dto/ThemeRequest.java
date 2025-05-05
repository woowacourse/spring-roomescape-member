package roomescape.dto;

import roomescape.entity.Theme;

public record ThemeRequest(
        String name,
        String description,
        String thumbnail
) {

    public Theme toEntity() {
        return new Theme(null, this.name(), this.description(), this.thumbnail());
    }
}
