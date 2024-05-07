package roomescape.controller.request;

import roomescape.exception.BadRequestException;

public class ThemeRequest {

    private final String name;
    private final String description;
    private final String thumbnail;

    public ThemeRequest(String name, String description, String thumbnail) {
        validate(name, description, thumbnail);
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    private void validate(String name, String description, String thumbnail) {
        validateNull(name);
        validateNull(description);
        validateNull(thumbnail);
    }

    private void validateNull(String value) {
        if (value == null || value.isBlank()) {
            throw new BadRequestException("[ERROR] 요청된 데이터에 null 혹은 비어있는 값이 존재합니다.");
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
