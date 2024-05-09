package roomescape.theme.domain;

import roomescape.name.domain.Name;

public class Theme {
    private long id;
    private final Name name;
    private final String description;
    private final String thumbnail;

    public Theme(long id, String name, String description, String thumbnail) {
        this.id = id;
        this.name = new Name(name);
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Theme(String name, String description, String thumbnail) {
        this(0, name, description, thumbnail);
    }

    public Theme(long id) {
        this(id, null, null, null);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setIdOnSave(long id) {
        this.id = id;
    }

}
