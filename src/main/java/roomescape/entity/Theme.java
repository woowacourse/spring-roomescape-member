package roomescape.entity;

import java.util.Objects;

public class Theme {

    private static final int DURING_TIME = 2;

    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public Theme(Long id, String name, String description, String thumbnail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Theme(String name, String description, String thumbnail) {
        this(null, name, description, thumbnail);
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public int getDuringTime() {
        return DURING_TIME;
    }

    @Override
    public String toString() {
        return "Theme{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", thumbnail='" + thumbnail + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Theme theme = (Theme) o;
        return Objects.equals(id, theme.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
