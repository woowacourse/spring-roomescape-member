package roomescape.domain;

import java.util.Objects;

public class Theme {
    private final Long id;
    private final ThemeName name;
    private final String description;
    private final String thumbnailUrl;

    public Theme(Long id, ThemeName name, String description, String thumbnailUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
    }

    public Theme(ThemeName name, String description, String thumbnail) {
        this(null, name, description, thumbnail);
    }

    public Theme withId(long id) {
        return new Theme(id, name, description, thumbnailUrl);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.asText();
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
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
