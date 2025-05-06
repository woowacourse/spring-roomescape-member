package roomescape.controller.dto.response;

import roomescape.service.dto.response.RoomThemeResult;

public record RoomThemeResponse(long id, String name, String description, String thumbnail) {

    public static RoomThemeResponse from(final RoomThemeResult result) {
        return new RoomThemeResponse(result.id(), result.name(), result.description(), result.thumbnail());
    }
}
