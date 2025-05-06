package roomescape.entity;

import java.util.Objects;

public class Theme {
    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public Theme(Long id, String name, String description, String thumbnail) {
        this.id = id;
        this.name = Objects.requireNonNull(name);
        this.description = Objects.requireNonNull(description);
        this.thumbnail = Objects.requireNonNull(thumbnail);
    }

    public Theme(String name, String description, String thumbnail) {
        this(null, name, description, thumbnail);
    }

    public Theme copyWithId(Long id) {
        return new Theme(id, name, description, thumbnail);
    }

    public Long getId() {
        if (id == null) {
            throw new NullPointerException("id값이 존재하지 않습니다.");
        }
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
