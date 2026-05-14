package roomescape.dto;

import roomescape.model.Theme;

public record ThemeResponse(Long id, String name, String description, String url) {

    public static ThemeResponse from(Theme theme) {
        return new ThemeResponse(theme.id(), theme.name(), theme.description(), theme.url());
    }

}
