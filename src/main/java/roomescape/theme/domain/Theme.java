package roomescape.theme.domain;

import java.util.Objects;

public class Theme {

    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    private Theme(final Long id, final String name, final String description, final String thumbnail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public static Theme createWithoutId(final String name, final String description, final String thumbnail) {
        return new Theme(null, name, description, thumbnail);
    }

    public static Theme createWithId(final Long id, final String name, final String description, final String thumbnail) {
        return new Theme(Objects.requireNonNull(id), name, description, thumbnail);
    }

    public Theme assignId(final Long id) {
        return new Theme(Objects.requireNonNull(id), name, description, thumbnail);
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
    public boolean equals(final Object object) {
        if (!(object instanceof Theme theme)) {
            return false;
        }
        return Objects.equals(getId(), theme.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
