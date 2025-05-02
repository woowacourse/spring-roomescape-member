package roomescape.domain.reservation.entity;

import roomescape.common.exception.InvalidArgumentException;

public class Theme {

    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public Theme(Long id, String name, String description, String thumbnail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
        validateTheme();
    }

    public static Theme withoutId(String name, String description, String thumbnail) {
        return new Theme(null, name, description, thumbnail);
    }

    public void validateTheme() {
        if (name == null || description == null || thumbnail == null) {
            throw new InvalidArgumentException("Theme field cannot be null");
        }
    }

    public boolean existId() {
        return id != null;
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
