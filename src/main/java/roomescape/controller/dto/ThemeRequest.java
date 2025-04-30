package roomescape.controller.dto;

import roomescape.service.reservation.Theme;

public record ThemeRequest(String name, String description, String thumbnail) {

    public Theme convertToTheme() {
        return new Theme(null, name, description, thumbnail);
    }
}
