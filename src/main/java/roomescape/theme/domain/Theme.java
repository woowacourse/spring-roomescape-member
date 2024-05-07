package roomescape.theme.domain;

import java.util.Objects;

import roomescape.exception.InvalidNameException;

public class Theme {
    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    private Theme(final Long id, final String name, final String description, final String thumbnail) {
        validateInvalidName(name);
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public static Theme createWithId(final Long id, final Theme theme) {
        return new Theme(id, theme.name, theme.description, theme.thumbnail);
    }

    public static Theme createWithId(final Long id, final String name, final String description, final String thumbnail) {
        return new Theme(id, name, description, thumbnail);
    }

    public static Theme createWithOutId(final String name, final String description, final String thumbnail) {
        return new Theme(null, name, description, thumbnail);
    }

    private void validateInvalidName(final String name) {
        if (Objects.isNull(name) || name.isBlank()) {
            throw new InvalidNameException("테마 이름이 null 이거나 공백인 경우 저장을 할 수 없습니다.");
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Theme theme = (Theme) o;
        return Objects.equals(id, theme.id) && Objects.equals(name, theme.name) && Objects.equals(description, theme.description) && Objects.equals(thumbnail, theme.thumbnail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, thumbnail);
    }
}
