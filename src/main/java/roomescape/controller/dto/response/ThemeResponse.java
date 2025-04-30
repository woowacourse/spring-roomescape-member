package roomescape.controller.dto.response;

import roomescape.domain.RoomTheme;

public record ThemeResponse(long id, String name, String description, String thumbnail) {

    public static ThemeResponse from(final RoomTheme theme) {
        return new ThemeResponse(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail());
    }
}
