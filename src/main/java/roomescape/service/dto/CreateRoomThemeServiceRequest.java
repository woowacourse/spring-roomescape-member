package roomescape.service.dto;

import roomescape.controller.dto.request.CreateRoomThemeRequest;

public record CreateRoomThemeServiceRequest(String name, String description, String thumbnail) {

    public static CreateRoomThemeServiceRequest from(final CreateRoomThemeRequest request) {
        return new CreateRoomThemeServiceRequest(request.name(), request.description(), request.thumbnail());
    }
}
