package roomescape.controller.dto.response;

import roomescape.service.dto.response.RoomThemeResult;

public record PopularThemeResponse(String name, String description, String thumbnail) {

    public static PopularThemeResponse from(RoomThemeResult result) {
        return new PopularThemeResponse(result.name(), result.description(), result.thumbnail());
    }
}
