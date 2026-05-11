package roomescape.controller.dto;

import roomescape.service.dto.ServiceThemeResponse;

public record ControllerThemeResponse(
        Long id,
        String name,
        String description,
        String thumbnailUrl
) {
    public static ControllerThemeResponse from(ServiceThemeResponse response) {
        return new ControllerThemeResponse(
                response.id(),
                response.name(),
                response.description(),
                response.thumbnailUrl()
        );
    }
}
