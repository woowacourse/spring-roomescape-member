package roomescape.domain;

import java.util.Objects;

import roomescape.domain.util.Validator;

public class Theme {
    private static final int MAX_STRING_LENGTH = 255;
    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public Theme(String name, String description, String thumbnail) {
        this(null, name, description, thumbnail);
    }

    public Theme(Long id, String name, String description, String thumbnail) {
        Validator.nonNull(name, description, thumbnail);
        Validator.notEmpty(name, thumbnail);
        Validator.overSize(MAX_STRING_LENGTH, name, description, thumbnail);

        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Theme createWithId(Long id) {
        return new Theme(id, name, description, thumbnail);
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

    public String thumbnail() {
        return thumbnail;
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
