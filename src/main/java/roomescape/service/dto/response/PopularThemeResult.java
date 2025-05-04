package roomescape.service.dto.response;

import roomescape.domain.RoomTheme;

public record PopularThemeResult(String name, String description, String thumbnail) {

    public static PopularThemeResult from(RoomTheme theme) {
        return new PopularThemeResult(theme.getName(), theme.getDescription(), theme.getThumbnail());
    }
}
