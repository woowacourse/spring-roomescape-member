package roomescape.domain;

import roomescape.domain.exception.Validate;

public class Theme {
    private final Validate validate = new Validate();
    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;


    public Theme(Long id, String name, String description, String thumbnail) {
        validate.AllNonNull(id, name, description, thumbnail);
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Theme(String name, String description, String thumbnail) {
        validate.AllNonNull(name, description, thumbnail);
        this.id = null;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Theme withId(Long id) {
        return new Theme(id, this.name, this.description, this.thumbnail);
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
