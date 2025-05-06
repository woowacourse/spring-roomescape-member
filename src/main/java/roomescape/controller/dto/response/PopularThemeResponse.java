package roomescape.controller.dto.response;

import roomescape.service.dto.response.PopularThemeResult;

public record PopularThemeResponse(String name, String description, String thumbnail) {

    public static PopularThemeResponse from(PopularThemeResult result) {
        return new PopularThemeResponse(result.name(), result.description(), result.thumbnail());
    }
}
