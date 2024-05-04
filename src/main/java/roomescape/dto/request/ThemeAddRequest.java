package roomescape.dto.request;

import roomescape.domain.Theme;

public record ThemeAddRequest(String name, String description, String thumbnail) {
    public Theme toEntity() {
        return new Theme(null, name, description, thumbnail);
    }
}
