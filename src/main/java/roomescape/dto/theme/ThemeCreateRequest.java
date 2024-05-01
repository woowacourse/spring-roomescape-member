package roomescape.dto.theme;

import java.util.Objects;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeDescription;
import roomescape.domain.theme.ThemeName;
import roomescape.domain.theme.ThemeThumbnail;

public class ThemeCreateRequest {

    private final String name;
    private final String description;
    private final String thumbnail;

    private ThemeCreateRequest(String name, String description, String thumbnail) {
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public static ThemeCreateRequest of(String name, String description, String thumbnail) {
        return new ThemeCreateRequest(name, description, thumbnail);
    }

    public Theme toDomain() {
        return new Theme(
                null,
                ThemeName.from(name),
                ThemeDescription.from(description),
                ThemeThumbnail.from(thumbnail)
        );
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
        ThemeCreateRequest other = (ThemeCreateRequest) o;
        return Objects.equals(this.name, other.name)
                && Objects.equals(this.description, other.description)
                && Objects.equals(this.thumbnail, other.thumbnail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, thumbnail);
    }

    @Override
    public String toString() {
        return "ThemeCreateRequest{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                '}';
    }
}
