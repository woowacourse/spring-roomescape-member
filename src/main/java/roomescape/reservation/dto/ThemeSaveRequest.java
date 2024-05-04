package roomescape.reservation.dto;

import roomescape.reservation.domain.Theme;
import roomescape.reservation.domain.ThemeName;

public record ThemeSaveRequest(String name, String description, String thumbnail) {

    public Theme toTheme() {
        return new Theme(new ThemeName(name), description, thumbnail);
    }
}
