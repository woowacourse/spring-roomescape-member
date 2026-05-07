package roomescape.controller.dto;

import roomescape.domain.Theme;

public record ThemeResponse(Long id, String name, String description, String thumbnailUrl) {
    public static ThemeResponse from(Theme theme) {
        return new ThemeResponse(
                theme.id(),
                theme.name(),
                theme.description(),
                theme.thumbnailUrl()
        );
    }
}
