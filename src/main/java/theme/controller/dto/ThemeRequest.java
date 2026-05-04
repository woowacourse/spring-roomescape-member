package theme.controller.dto;

import theme.service.ThemeCommand;

public record ThemeRequest(String name, String description, String thumbnailUrl) {

    public ThemeCommand toCommand() {
        return new ThemeCommand(name, description, thumbnailUrl);
    }
}
