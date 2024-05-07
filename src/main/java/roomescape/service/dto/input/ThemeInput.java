package roomescape.service.dto.input;

import roomescape.domain.Theme;

public record ThemeInput(String name, String description, String thumbnail) {

    public Theme toTheme() {
        return Theme.of(null, name, description, thumbnail);
    }
}
