package roomescape.domain;

import java.util.Objects;

public class Theme {
    private final Long id;
    private final ThemeName themeName;
    private final String description;
    private final String thumbnailUrl;

    public Theme(Long id, ThemeName themeName, String description, String thumbnailUrl) {
        this.id = id;
        this.themeName = themeName;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
    }

    public Theme(ThemeName themeName, String description, String thumbnail) {
        this(null, themeName, description, thumbnail);
    }

    public Theme withId(long id) {
        return new Theme(id, themeName, description, thumbnailUrl);
    }

    public Long getId() {
        return id;
    }

    public String getThemeName() {
        return themeName.asText();
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
