package roomescape.theme.controller.dto;

import roomescape.theme.domain.RoomTheme;

public record PopularThemeResponse(String name, String description, String thumbnail) {

    public static PopularThemeResponse from(final RoomTheme theme) {
        return new PopularThemeResponse(theme.getName(), theme.getDescription(), theme.getThumbnail());
    }
}
