package roomescape.dto;

import roomescape.domain.Theme;

public record ThemeCreateRequest(
        Long id,
        String name,
        String description,
        String thumbnail
) {

    public static Theme toTheme(final ThemeCreateRequest request) {
        return new Theme(request.id(), request.name(), request.description(), request.thumbnail());
    }
}
