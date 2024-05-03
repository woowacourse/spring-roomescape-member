package roomescape.dto;

import roomescape.domain.Theme;

public record ThemeRequest(
        Long id,
        String name,
        String description,
        String thumbnail
) {

    public static Theme toTheme(final ThemeRequest request) {
        return new Theme(request.id(), request.name(), request.description(), request.thumbnail());
    }
}
