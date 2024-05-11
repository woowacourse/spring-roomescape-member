package roomescape.domain;

import roomescape.domain.exception.InvalidNameException;

import java.util.Objects;

public class Theme {

    public static String DEFAULT_THUMBNAIL = "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg";

    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public Theme(Long id) {
        this(id, null, null, null);
    }

    public Theme(Long id, String name, String description, String thumbnail) {
        validateName(name);

        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = getDefaultThumbnailIfNotExists(thumbnail);
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidNameException("이름은 공백일 수 없습니다.");
        }
    }

    private String getDefaultThumbnailIfNotExists(String thumbnail) {
        if (thumbnail == null || thumbnail.isBlank()) {
            return DEFAULT_THUMBNAIL;
        }
        return thumbnail;
    }

    public Theme assignId(Long id) {
        return new Theme(id, name, description, thumbnail);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Theme theme = (Theme) o;
        return Objects.equals(id, theme.id) &&
                Objects.equals(name, theme.name) &&
                Objects.equals(description, theme.description) &&
                Objects.equals(thumbnail, theme.thumbnail);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(description);
        result = 31 * result + Objects.hashCode(thumbnail);
        return result;
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
}
