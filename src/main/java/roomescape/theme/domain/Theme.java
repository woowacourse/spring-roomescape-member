package roomescape.theme.domain;

import java.util.Objects;
import roomescape.exception.BadRequestException;
import roomescape.exception.ExceptionCause;

public class Theme {

    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    private Theme(Long id, String name, String description, String thumbnail) {
        validateFields(name, description, thumbnail);
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public static Theme create(final String name, final String description, final String thumbnail) {
        return new Theme(null, name, description, thumbnail);
    }

    public static Theme load(final Long id, final String name, final String description, final String thumbnail) {
        return new Theme(id, name, description, thumbnail);
    }

    private void validateFields(String name, String description, String thumbnail) {
        if (name.isBlank()) {
            throw new BadRequestException(ExceptionCause.EMPTY_VALUE_THEME_NAME);
        }
        if (description.isBlank()) {
            throw new BadRequestException(ExceptionCause.EMPTY_VALUE_THEME_DESCRIPTION);
        }
        if (thumbnail.isBlank()) {
            throw new BadRequestException(ExceptionCause.EMPTY_VALUE_THEME_THUMBNAIL);
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
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Theme theme = (Theme) o;
        return Objects.equals(id, theme.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
