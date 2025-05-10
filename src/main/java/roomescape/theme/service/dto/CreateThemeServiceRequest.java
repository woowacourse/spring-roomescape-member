package roomescape.theme.service.dto;

import roomescape.theme.controller.dto.CreateThemeRequest;

public record CreateThemeServiceRequest(String name, String description, String thumbnail) {

    public static CreateThemeServiceRequest from(final CreateThemeRequest request) {
        return new CreateThemeServiceRequest(request.name(), request.description(), request.thumbnail());
    }
}
