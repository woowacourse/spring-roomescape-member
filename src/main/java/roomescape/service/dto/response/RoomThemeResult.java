package roomescape.service.dto.response;

import roomescape.domain.RoomTheme;

public record RoomThemeResult(long id, String name, String description, String thumbnail) {

    public static RoomThemeResult from(final RoomTheme theme) {
        return new RoomThemeResult(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail());
    }
}
