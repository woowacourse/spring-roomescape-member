package roomescape.controller.dto;

import roomescape.service.dto.ThemeCreateCommand;

public class ThemeRequest {

    private String name;
    private String description;
    private String thumbnailUrl;

    public ThemeRequest() {
    }

    public ThemeCreateCommand toCommand() {
        return new ThemeCreateCommand(name, description, thumbnailUrl);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
}
