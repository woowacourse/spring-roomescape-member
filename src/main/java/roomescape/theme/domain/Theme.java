package roomescape.theme.domain;

import roomescape.global.exception.RoomEscapeException.BadRequestException;

public class Theme {
    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public Theme(Long id, String name, String description, String thumbnail) {
        this.id = id;
        validateName(name);
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public void validateName(String name) {
        if (name == null) {
            throw new BadRequestException("테마 이름이 비어있습니다.");
        }
        if (name.isBlank() || name.length() > 10) {
            throw new BadRequestException("테마 이름은 1자에서 10자 이내여야 합니다.");
        }
    }

    public Long getId() {
        return id;
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
