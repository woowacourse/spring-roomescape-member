package roomescape.domain.theme;

import java.util.Objects;

public class Theme {
    private final Long id;
    private final ThemeName name;
    private final Description description;
    private final Thumbnail thumbnail;

    public Theme(String name, String description) {
        this(null, new ThemeName(name), new Description(description), Thumbnail.empty());
    }

    public Theme(String name, String description, String thumbnail) {
        this(null, new ThemeName(name), new Description(description), new Thumbnail(thumbnail));
    }

    public Theme(Long id, String name, String description, String thumbnail) {
        this(id, new ThemeName(name), new Description(description), new Thumbnail(thumbnail));
    }

    private Theme(Long id, ThemeName name, Description description, Thumbnail thumbnail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.value();
    }

    public String getDescription() {
        return description.value();
    }

    public String getThumbnail() {
        return thumbnail.value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Theme theme = (Theme) o;
        return Objects.equals(id, theme.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
