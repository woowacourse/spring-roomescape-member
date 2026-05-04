package roomescape.dto;

import roomescape.domain.Theme;

public record ThemeRequest(
        String name,
        String description,
        String thumbnail
) {
    public Theme toTheme() {
        return Theme.createWithoutId(
                name,
                description,
                thumbnail
        );
    }
}
