package roomescape.controller.theme;

import roomescape.domain.Theme;

public record ThemeRequest(String name, String description, String thumbnail) {

    public Theme toDomain() {
        return new Theme(null, name, description, thumbnail);
    }
}
