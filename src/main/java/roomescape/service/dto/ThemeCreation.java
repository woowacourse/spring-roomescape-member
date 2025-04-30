package roomescape.service.dto;

import roomescape.controller.dto.request.CreateThemeRequest;

public record ThemeCreation(String name, String description, String thumbnail) {

    public static ThemeCreation from(CreateThemeRequest request) {
        return new ThemeCreation(request.name(), request.description(), request.thumbnail());
    }
}
