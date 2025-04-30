package roomescape.service.dto;

import roomescape.controller.dto.request.CreateThemeRequest;

public record RoomThemeCreation(String name, String description, String thumbnail) {

    public static RoomThemeCreation from(final CreateThemeRequest request) {
        return new RoomThemeCreation(request.name(), request.description(), request.thumbnail());
    }
}
