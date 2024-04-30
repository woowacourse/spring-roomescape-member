package roomescape.domain;

import java.util.Objects;

public class Theme {
    private static final Long DEFAULT_ID_VALUE = 0L;

    private final long id;
    private final ThemeName name;
    private final ThemeDescription description;
    private final String thumbnail;

    public Theme(final ThemeName name, final ThemeDescription description, final String thumbnail) {
        this(DEFAULT_ID_VALUE, name, description, thumbnail);
    }

    public Theme(final long id, final ThemeName name, final ThemeDescription description, final String thumbnail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Theme initializeIndex(final long themeId) {
        return new Theme(themeId, name, description, thumbnail);
    }

    public long getId() {
        return id;
    }

    public ThemeName getName() {
        return name;
    }

    public ThemeDescription getDescription() {
        return description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final Theme theme)) return false;
        return id == theme.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
