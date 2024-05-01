package roomescape.controller.theme;

import roomescape.domain.Theme;

public record CreateThemeRequest(String name, String description, String thumbnail) {

    public Theme toDomain() {
        return Theme.from(name, description, thumbnail);
    }
}
