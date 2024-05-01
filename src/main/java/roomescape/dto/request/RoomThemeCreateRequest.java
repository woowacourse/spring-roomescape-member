package roomescape.dto.request;

import roomescape.domain.RoomTheme;

public record RoomThemeCreateRequest(String name, String description, String thumbnail) {
    public RoomTheme toRoomTheme() {
        return new RoomTheme(name, description, thumbnail);
    }
}
