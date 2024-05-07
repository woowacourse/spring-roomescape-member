package roomescape.domain;

import roomescape.domain.exception.InvalidDomainObjectException;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;

public class Theme {
    private static final Pattern URL_PATTERN = Pattern.compile(
            "^http(s)?:\\/\\/.*\\.(?:png|jpe?g|gif|bmp)$");
    private static final int NAME_LENGTH_MIN = 2;
    private static final int NAME_LENGTH_MAX = 20;
    private static final int DESCRIPTION_LENGTH_MAX = 255;
    private static final int THUMBNAIL_LENGTH_MAX = 255;

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

    private void validate(String name, String description, String thumbnail) {
        validateName(name);
        validateDescription(description);
        validateThumbnail(thumbnail);
    }

    private void validateName(String name) {
        if (isNull(name)) {
            throw new InvalidDomainObjectException("name must not be null");
        }
        if (NAME_LENGTH_MAX < name.length() || name.length() < NAME_LENGTH_MIN) {
            throw new InvalidDomainObjectException(String.format("name must be between %d and %d characters",
                    NAME_LENGTH_MIN, NAME_LENGTH_MAX));
        }
    }

    private void validateDescription(String description) {
        if (isNull(description)) {
            throw new InvalidDomainObjectException("description must not be null");
        }
        if (DESCRIPTION_LENGTH_MAX < description.length()) {
            throw new InvalidDomainObjectException(String.format("description must be less than %d characters",
                    DESCRIPTION_LENGTH_MAX));
        }
    }

    private void validateThumbnail(String thumbnail) {
        if (isNull(thumbnail)) {
            return;
        }
        if (THUMBNAIL_LENGTH_MAX < thumbnail.length()) {
            throw new InvalidDomainObjectException(String.format("thumbnail must be less than %d characters",
                    THUMBNAIL_LENGTH_MAX));
        }
        Matcher matcher = URL_PATTERN.matcher(thumbnail);
        if (!matcher.matches()) {
            throw new InvalidDomainObjectException("thumbnail must be a valid URL");
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Theme theme = (Theme) o;
        return Objects.equals(id, theme.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
