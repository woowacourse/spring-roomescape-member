package roomescape.domain;

import lombok.Getter;
import roomescape.exception.DomainException;
import roomescape.exception.ErrorCode;

@Getter
public class Theme {
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
            throw new DomainException(ErrorCode.THEME_ALREADY_HAS_ID);
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
            throw new DomainException(ErrorCode.INVALID_THEME_ID);
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new DomainException(ErrorCode.INVALID_THEME_NAME);
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new DomainException(ErrorCode.INVALID_THEME_DESCRIPTION);
        }
    }

    private void validateThumbnail(String thumbnail) {
        if (thumbnail == null || thumbnail.isBlank()) {
            throw new DomainException(ErrorCode.INVALID_THEME_THUMBNAIL);
        }
    }
}
