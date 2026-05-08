package roomescape.theme.domain;

import lombok.Getter;
import roomescape.global.exception.InvalidRequestException;

@Getter
public class Theme {
    private static final String INVALID_THEME_ID_MESSAGE = "테마 id는 비어 있을 수 없습니다.";
    private static final String INVALID_THEME_NAME_MESSAGE = "테마 이름은 비어 있을 수 없습니다.";
    private static final String INVALID_THEME_DESCRIPTION_MESSAGE = "테마 설명은 비어 있을 수 없습니다.";
    private static final String INVALID_THEME_THUMBNAIL_MESSAGE = "테마 썸네일은 비어 있을 수 없습니다.";
    private static final String THEME_ALREADY_HAS_ID_MESSAGE = "이미 id가 존재하는 테마입니다.";

    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public Theme(String name, String description, String thumbnail) {
        this(null, name, description, thumbnail);
    }

    public Theme(Long id, String name, String description, String thumbnail) {
        validate(name, description, thumbnail);
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Theme withId(Long id) {
        validateId(id);

        if (this.id != null) {
            throw new InvalidRequestException(THEME_ALREADY_HAS_ID_MESSAGE);
        }

        return new Theme(id, name, description, thumbnail);
    }

    private void validate(String name, String description, String thumbnail) {
        validateName(name);
        validateDescription(description);
        validateThumbnail(thumbnail);
    }

    private void validateId(Long id) {
        if (id == null) {
            throw new InvalidRequestException(INVALID_THEME_ID_MESSAGE);
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidRequestException(INVALID_THEME_NAME_MESSAGE);
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new InvalidRequestException(INVALID_THEME_DESCRIPTION_MESSAGE);
        }
    }

    private void validateThumbnail(String thumbnail) {
        if (thumbnail == null || thumbnail.isBlank()) {
            throw new InvalidRequestException(INVALID_THEME_THUMBNAIL_MESSAGE);
        }
    }
}
