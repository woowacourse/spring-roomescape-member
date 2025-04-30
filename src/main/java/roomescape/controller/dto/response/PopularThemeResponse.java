package roomescape.controller.dto.response;

import roomescape.domain.RoomTheme;

public record PopularThemeResponse(String name, String description, String thumbnail) {

    public static PopularThemeResponse from(RoomTheme theme) {
        return new PopularThemeResponse(theme.getName(), theme.getDescription(), theme.getThumbnail());
    }
}
