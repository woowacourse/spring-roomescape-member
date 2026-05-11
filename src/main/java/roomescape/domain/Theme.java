package roomescape.domain;

import java.util.Objects;
import roomescape.domain.vo.ThemeImageUrl;
import roomescape.domain.vo.ThemeName;

public class Theme {

    private final Long id;
    private final ThemeName name;
    private final String description;
    private final ThemeImageUrl imageUrl;

    public Theme(Long id, ThemeName name, String description, ThemeImageUrl imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public Theme(String name, String description, String imageUrl) {
        this(null, new ThemeName(name), description, new ThemeImageUrl(imageUrl));
    }

    public Long getId() {
        return id;
    }

    public String getNameValue() {
        return name.value();
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrlValue() {
        return imageUrl.value();
    }

    public Theme withId(long id) {
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
