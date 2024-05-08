package roomescape.dto.request;

import roomescape.domain.roomescape.Theme;

public record ThemeSaveRequest(
        String name,
        String description,
        String thumbnail
) {
    public Theme toEntity() {
        return new Theme(name, description, thumbnail);
    }
}
