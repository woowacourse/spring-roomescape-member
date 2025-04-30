package roomescape.entity;

import roomescape.exception.impl.IdCannotBeNullException;
import roomescape.exception.impl.ThemeNameMaxLengthExceedException;

public class Theme {

    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    private Theme(
            final Long id,
            final String name,
            final String description,
            final String thumbnail
    ) {
        if (name.length() > 20) {
            throw new ThemeNameMaxLengthExceedException();
        }
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public static Theme beforeSave(
            final String name,
            final String description,
            final String thumbnail
    ) {
        return new Theme(null, name, description, thumbnail);
    }

    public static Theme afterSave(
            final Long id,
            final String name,
            final String description,
            final String thumbnail
    ) {
        if (id == null) {
            throw new IdCannotBeNullException();
        }
        return new Theme(id, name, description, thumbnail);
    }

    public static Theme afterSave(final Long id, final Theme theme) {
        return afterSave(id, theme.name, theme.description, theme.thumbnail);
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
