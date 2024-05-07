package roomescape.dto.response;

import roomescape.domain.RoomTheme;

public record RoomThemeResponse(
        Long id,
        String name,
        String description,
        String thumbnail) {
    public static RoomThemeResponse fromRoomTheme(RoomTheme roomTheme) {
        return new RoomThemeResponse(
                roomTheme.getId(),
                roomTheme.getName(),
                roomTheme.getDescription(),
                roomTheme.getThumbnail());
    }
}
