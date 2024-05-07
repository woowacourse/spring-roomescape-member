package roomescape.service.dto.request;

import roomescape.domain.Theme;

public record ThemeRequest(String name, String description, String thumbnail) {

    public Theme toEntity() {
        return new Theme(null, name, description, thumbnail);
    }
}
