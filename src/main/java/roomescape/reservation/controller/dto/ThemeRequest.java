package roomescape.reservation.controller.dto;

import roomescape.reservation.domain.Theme;

public record ThemeRequest(String name, String description, String thumbnail) {

    public Theme toThemeWithoutId() {
        return new Theme(null, name, description, thumbnail);
    }
}
