package roomescape.theme.controller.dto;

import roomescape.theme.domain.Theme;

public record PopularThemeResponse(String name, String description, String thumbnail) {

    public static PopularThemeResponse from(final Theme theme) {
        return new PopularThemeResponse(theme.getName(), theme.getDescription(), theme.getThumbnail());
    }
}
