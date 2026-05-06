package roomescape.request;

import roomescape.domain.Theme;

public record ThemeRequest(Long id, String name, String description, String thumbnailUrl) {
    public Theme domain() {
        return new Theme(null, name, description, thumbnailUrl);
    }
}
