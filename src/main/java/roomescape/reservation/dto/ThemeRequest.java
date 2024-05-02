package roomescape.reservation.dto;

import roomescape.reservation.domain.Name;
import roomescape.reservation.domain.Theme;

public record ThemeRequest(String name, String description, String thumbnail) {

    public Theme toTheme() {
        return new Theme(new Name(name), description, thumbnail);
    }
}
