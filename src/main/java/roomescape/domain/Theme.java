package roomescape.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import roomescape.exception.InvalidInputException;

public class Theme {
    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    @JsonCreator
    public Theme(Long id, String name, String description, String thumbnail) {
        validate(name, description, thumbnail);
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Theme(String name, String description, String thumbnail) {
        this(null, name, description, thumbnail);
    }

    private void validate(String name, String description, String thumbnail) {
        if (name == null || name.isBlank()) {
            throw new InvalidInputException("테마명이 입력되지 않았습니다.");
        }
        if (description == null || description.isBlank()) {
            throw new InvalidInputException("테마 설명이 입력되지 않았습니다.");
        }
        if (thumbnail == null || thumbnail.isBlank()) {
            throw new InvalidInputException("테마 이미지가 입력되지 않았습니다.");
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
