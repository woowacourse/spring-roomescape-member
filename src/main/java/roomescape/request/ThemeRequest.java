package roomescape.request;

import roomescape.domain.Theme;

public record ThemeRequest(String name, String description, String thumbnailUrl) {
    public Theme toDomainForSave() {
        return new Theme(null, name, description, thumbnailUrl);
    }
}
