package roomescape.domain;

import roomescape.exceptions.InvalidRequestBodyFieldException;

public class Theme {

    private Long id;
    private String name;
    private String description;
    private String thumbnail;

    private Theme() {
    }

    public Theme(String name, String description, String thumbnail) {
        this(null, name, description, thumbnail);
    }

    public Theme(Long id, String name, String description, String thumbnail) {
        validateNotnull(name, description, thumbnail);
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    private static void validateNotnull(String name, String description, String thumbnail) {
        if (name == null || name.isBlank() || description == null || thumbnail == null) {
            throw new InvalidRequestBodyFieldException("테마 필드값이 null 입니다.");
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
