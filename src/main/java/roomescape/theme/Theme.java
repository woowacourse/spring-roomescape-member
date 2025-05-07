package roomescape.theme;

import java.util.Objects;
import roomescape.exception.ArgumentNullException;

public class Theme {
    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public Theme(Long id, String name, String description, String thumbnail) {
        validateNull(name, description, thumbnail);
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    private void validateNull(String name, String description, String thumbnail) {
        if (name == null || name.isBlank()) {
            throw new ArgumentNullException();
        }
        if (description == null || description.isBlank()) {
            throw new ArgumentNullException();
        }
        if (thumbnail == null || thumbnail.isBlank()) {
            throw new ArgumentNullException();
        }
    }

    public static Theme createWithoutId(String name, String description, String thumbnail) {
        validateDescriptionLength(description);
        return new Theme(null, name, description, thumbnail);
    }

    private static void validateDescriptionLength(String description) {
        if (description.length() < 5 || description.length() > 100) {
            throw new IllegalArgumentException("테마 소개는 최소 5글자, 최대 100글자여야 합니다.");
        }
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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Theme theme = (Theme) o;
        return Objects.equals(id, theme.id) && Objects.equals(name, theme.name)
                && Objects.equals(description, theme.description) && Objects.equals(thumbnail,
                theme.thumbnail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, thumbnail);
    }
}
