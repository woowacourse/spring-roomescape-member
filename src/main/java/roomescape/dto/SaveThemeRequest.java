package roomescape.dto;

import roomescape.domain.Theme;

public record SaveThemeRequest(String name, String description, String thumbnail) {
    public Theme toTheme() {
        return Theme.of(name, description, thumbnail);
    }
}
