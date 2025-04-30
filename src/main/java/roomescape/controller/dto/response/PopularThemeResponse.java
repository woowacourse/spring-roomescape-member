package roomescape.controller.dto.response;

import roomescape.domain.Theme;

public record PopularThemeResponse(String name, String description, String thumbnail) {

    public static PopularThemeResponse from(Theme theme) {
        return new PopularThemeResponse(theme.getName(), theme.getDescription(), theme.getThumbnail());
    }
}
