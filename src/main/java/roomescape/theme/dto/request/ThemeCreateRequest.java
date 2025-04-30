package roomescape.theme.dto.request;

import roomescape.theme.domain.Theme;

public record ThemeCreateRequest(
        String name,
        String description,
        String thumbnail
) {
    public Theme toTheme() {
        return Theme.withUnassignedId(name, description, thumbnail);
    }
}
