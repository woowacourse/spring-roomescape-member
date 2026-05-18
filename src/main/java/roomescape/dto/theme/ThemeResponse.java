package roomescape.dto.theme;

import roomescape.domain.theme.Theme;

public record ThemeResponse(long id, String name, String description, String imageUrl) {

    public static ThemeResponse from(Theme theme) {
        return new ThemeResponse(
                theme.id(),
                theme.name(),
                theme.description(),
                theme.imageUrl());
    }
}
