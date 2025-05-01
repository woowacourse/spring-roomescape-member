package roomescape.model.entity;

import roomescape.exception.impl.ThemeNameMaxLengthExceedException;
import roomescape.model.vo.Id;

public class Theme {

    private static final int MAX_NAME_LENGTH = 20;

    private final Id id;
    private final String name;
    private final String description;
    private final String thumbnail;

    private Theme(
            final Id id,
            final String name,
            final String description,
            final String thumbnail
    ) {
        validateMaxNameLength(name);
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    private void validateMaxNameLength(final String name) {
        if (name.length() > MAX_NAME_LENGTH) {
            throw new ThemeNameMaxLengthExceedException();
        }
    }

    public static Theme beforeSave(
            final String name,
            final String description,
            final String thumbnail
    ) {
        return new Theme(Id.nullId(), name, description, thumbnail);
    }

    public static Theme afterSave(
            final long id,
            final String name,
            final String description,
            final String thumbnail
    ) {
        return new Theme(Id.create(id), name, description, thumbnail);
    }

    public static Theme afterSave(final long id, final Theme theme) {
        return afterSave(id, theme.name, theme.description, theme.thumbnail);
    }

    public Long getId() {
        return id.longValue();
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
