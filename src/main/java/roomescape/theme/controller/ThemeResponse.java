package roomescape.theme.controller;

import roomescape.theme.domain.Theme;

public record ThemeResponse(long id, String name, String description, String thumbnail) {
    public static ThemeResponse from(Theme theme) {
        return new ThemeResponse(theme.id(), theme.name(), theme.description(), theme.thumbnail());
    }
}
