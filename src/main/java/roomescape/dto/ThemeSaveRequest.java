package roomescape.dto;

import roomescape.domain.Theme;

public record ThemeSaveRequest(String name, String description, String thumbnail) {

    public Theme toModel() {
        return new Theme(name, description, thumbnail);
    }
}
