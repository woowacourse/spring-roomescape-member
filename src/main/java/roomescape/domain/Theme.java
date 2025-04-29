package roomescape.domain;

import java.util.Objects;

public class Theme {
    private final long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public Theme(final long id, final String name, final String description, final String thumbnail) {
        validate(name, description, thumbnail);

        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Theme(final String name, final String description, final String thumbnail) {
        this(0, name, description, thumbnail);
    }

    private void validate(String name, String description, String thumbnail) {
        if (Objects.isNull(name) || name.isBlank()) {
            throw new IllegalArgumentException("[ERROR]");
        }
        if (Objects.isNull(description) || description.isBlank()) {
            throw new IllegalArgumentException("[ERROR]");
        }
        if (Objects.isNull(thumbnail) || thumbnail.isBlank()) {
            throw new IllegalArgumentException("[ERROR]");
        }
    }

    public Theme withId(final long id) {
        return new Theme(id, this.name, this.description, this.thumbnail);
    }

    public long getId() {
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
