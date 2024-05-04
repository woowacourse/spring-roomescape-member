package roomescape.controller.request;

import roomescape.exception.BadRequestException;

public class ThemeRequest {

    private final String name;
    private final String description;
    private final String thumbnail;

    public ThemeRequest(String name, String description, String thumbnail) {
        validateNull(name);
        validateNull(description);
        validateNull(thumbnail);
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    private void validateNull(String value) {
        if (value == null || value.isEmpty()) {
            throw new BadRequestException("값이 null이 될 수 없습니다.");
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
