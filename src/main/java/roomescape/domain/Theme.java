package roomescape.domain;

import java.util.Objects;

import roomescape.domain.util.Validator;

public record Theme(Long id, String name, String description, String thumbnail) {
    private static final int MAX_STRING_LENGTH = 255;

    public Theme(String name, String description, String thumbnail) {
        this(null, name, description, thumbnail);
    }

    public Theme {
        Validator.nonNull(name, description, thumbnail);
        Validator.notEmpty(name, thumbnail);
        Validator.overSize(MAX_STRING_LENGTH, name, description, thumbnail);
    }

    public Theme createWithId(Long id) {
        return new Theme(id, name, description, thumbnail);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Theme theme = (Theme) o;
        if (id == null || theme.id == null)
            return Objects.equals(name, theme.name);
        return Objects.equals(id, theme.id);
    }

    @Override
    public int hashCode() {
        if (id == null)
            return Objects.hash(name);
        return Objects.hash(id);
    }
}
