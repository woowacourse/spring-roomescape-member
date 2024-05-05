package roomescape.model;

import java.util.Objects;

public class Theme {

    private Long themeId;
    private String name;
    private String description;
    private String thumbnail;

    private Theme() {
    }

    public Theme(Long themeId, String name, String description, String thumbnail) {
        this.themeId = themeId;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Theme(String name, String description, String thumbnail) {
        this(null, name, description, thumbnail);
    }

    public long getThemeId() {
        return themeId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Theme theme = (Theme) o;
        return themeId.equals(theme.themeId)
                && Objects.equals(name, theme.name)
                && Objects.equals(description, theme.description)
                && Objects.equals(thumbnail, theme.thumbnail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(themeId, name, description, thumbnail);
    }

    @Override
    public String toString() {
        return "Theme{" +
                "themeId=" + themeId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                '}';
    }
}
