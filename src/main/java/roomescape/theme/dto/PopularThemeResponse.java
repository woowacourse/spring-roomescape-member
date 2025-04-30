package roomescape.theme.dto;

import roomescape.theme.domain.Theme;

public record PopularThemeResponse(String name, String description, String thumbnail) {
    public static PopularThemeResponse from(Theme theme) {
        return new PopularThemeResponse(theme.name(), theme.description(), theme.thumbnail());
    }
}
