package roomescape.entity;

import roomescape.exception.impl.IdCannotBeNullException;
import roomescape.exception.impl.ThemeNameMaxLengthExceedException;

public class Theme {

    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    private Theme(Long id, String name, String description, String thumbnail) {
        if (name.length() > 20) {
            throw new ThemeNameMaxLengthExceedException();
        }
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public static Theme beforeSave(String name, String description, String thumbnail) {
        return new Theme(null, name, description, thumbnail);
    }

    public static Theme afterSave(Long id, String name, String description, String thumbnail) {
        if (id == null) {
            throw new IdCannotBeNullException();
        }
        return new Theme(id, name, description, thumbnail);
    }
}
