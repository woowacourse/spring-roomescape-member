package roomescape.controller.theme;

import roomescape.domain.Theme;

public record PopularThemeResponse(String name, String thumbnail, String description) {

    public static PopularThemeResponse from(Theme theme) {
        return new PopularThemeResponse(theme.getName(), theme.getThumbnail(), theme.getDescription());
    }
}
