package roomescape.domain;

import roomescape.exception.InvalidInputException;

public class RoomTheme {
    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public RoomTheme(String name, String description, String thumbnail) {
        this(null, name, description, thumbnail);
    }

    public RoomTheme(Long id, RoomTheme roomTheme) {
        this(id, roomTheme.name, roomTheme.description, roomTheme.thumbnail);
    }

    public RoomTheme(Long id, String name, String description, String thumbnail) {
        validateBlank(name, description, thumbnail);
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    private void validateBlank(String name, String description, String thumbnail) {
        if (name == null || name.isBlank()) {
            throw new InvalidInputException("테마 이름에 공백을 입력할 수 없습니다.");
        }
        if (description == null || description.isBlank()) {
            throw new InvalidInputException("테마 설명에 공백을 입력할 수 없습니다.");
        }
        if (thumbnail == null || thumbnail.isBlank()) {
            throw new InvalidInputException("테마 썸네일에 공백을 입력할 수 없습니다.");
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
