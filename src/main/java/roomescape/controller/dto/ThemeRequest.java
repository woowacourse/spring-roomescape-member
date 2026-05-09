package roomescape.controller.dto;

import roomescape.service.dto.ThemeCreateCommand;

public record ThemeRequest(
        String name,
        String description,
        String thumbnailUrl
) {
    public ThemeCreateCommand toCommand() {
        return new ThemeCreateCommand(name, description, thumbnailUrl);
    }
}
