package roomescape.theme;

import roomescape.exception.ArgumentNullException;

public class Theme {
    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public Theme(Long id, String name, String description, String thumbnail) {
        validateNull(name, description, thumbnail);
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    private void validateNull(String name, String description, String thumbnail) {
        if (name == null || name.isBlank()) {
            throw new ArgumentNullException();
        }
        if (description == null || description.isBlank()) {
            throw new ArgumentNullException();
        }
        if (thumbnail == null || thumbnail.isBlank()) {
            throw new ArgumentNullException();
        }
    }

    public static Theme createWithoutId(String name, String description, String thumbnail) {
        return new Theme(null, name, description, thumbnail);
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
