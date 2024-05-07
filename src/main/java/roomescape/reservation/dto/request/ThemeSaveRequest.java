package roomescape.reservation.dto.request;

import roomescape.reservation.domain.Theme;

public record ThemeSaveRequest(String name, String description, String thumbnail) {

    public Theme toModel() {
        return new Theme(name, description, thumbnail);
    }
}
