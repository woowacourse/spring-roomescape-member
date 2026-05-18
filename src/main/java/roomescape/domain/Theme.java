package roomescape.domain;

import roomescape.domain.vo.ThemeImageUrl;
import roomescape.domain.vo.ThemeName;

import java.util.Objects;

public class Theme {

    private final Long id;
    private final ThemeName name;
    private final String description;
    private final ThemeImageUrl imageUrl;

    public Theme(Long id, ThemeName name, String description, ThemeImageUrl imageUrl) {
        this.id = id;
        this.name = Objects.requireNonNull(name);
        this.description = description;
        this.imageUrl = Objects.requireNonNull(imageUrl);
    }

    public Theme(ThemeName name, String description, ThemeImageUrl imageUrl) {
        this(null, name, description, imageUrl);
    }

    public Long getId() {
        return id;
    }

    public ThemeName getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ThemeImageUrl getImageUrl() {
        return imageUrl;
    }

    public Theme withId(Long id) {
        return new Theme(id, this.name, this.description, this.imageUrl);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Theme theme = (Theme) o;
        return id != null
                && Objects.equals(id, theme.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
