package roomescape.theme.controller.dto;

import roomescape.theme.domain.RoomTheme;

public record RoomThemeResponse(long id, String name, String description, String thumbnail) {

    public static RoomThemeResponse from(final RoomTheme theme) {
        return new RoomThemeResponse(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail());
    }
}
