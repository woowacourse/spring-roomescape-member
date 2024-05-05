package roomescape.controller.request;

import roomescape.exception.BadRequestException;

public class ThemeRequest {

    private final String name;
    private final String description;
    private final String thumbnail;

    public ThemeRequest(String name, String description, String thumbnail) {
        validateNull(name, "name");
        validateNull(description, "description");
        validateNull(thumbnail, "thumbnail");
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    private void validateNull(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new BadRequestException(value, fieldName);
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
