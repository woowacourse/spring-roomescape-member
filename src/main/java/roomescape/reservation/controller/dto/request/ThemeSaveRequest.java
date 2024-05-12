package roomescape.reservation.controller.dto.request;

import roomescape.reservation.domain.Theme;

public record ThemeSaveRequest(
        String name,
        String description,
        String thumbnail
) {
    public Theme toEntity() {
        return new Theme(name, description, thumbnail);
    }
}
