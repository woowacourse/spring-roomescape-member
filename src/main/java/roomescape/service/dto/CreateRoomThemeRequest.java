package roomescape.service.dto;

public record CreateRoomThemeRequest(String name, String description, String thumbnail) {

    public static CreateRoomThemeRequest from(final roomescape.controller.dto.request.CreateRoomThemeRequest request) {
        return new CreateRoomThemeRequest(request.name(), request.description(), request.thumbnail());
    }
}
