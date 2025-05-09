package roomescape.dto.reservation;

import roomescape.entity.reservation.Theme;

public record ThemeRequest(
        String name,
        String description,
        String thumbnail
) {

    public Theme toEntity() {
        return new Theme(null, this.name(), this.description(), this.thumbnail());
    }
}
