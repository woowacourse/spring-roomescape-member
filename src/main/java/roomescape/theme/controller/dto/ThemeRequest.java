package roomescape.theme.controller.dto;

import roomescape.theme.service.ThemeCommand;

public record ThemeRequest(String name, String description, String thumbnailUrl) {

    public ThemeCommand toCommand() {
        return new ThemeCommand(name, description, thumbnailUrl);
    }
}
