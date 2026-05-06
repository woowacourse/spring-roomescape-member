package roomescape.theme.entity;

import java.time.Duration;
import java.util.Objects;

public class Theme {

    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnailUrl;
    private final Duration runtime;

    private Theme(Long id, String name, String description, String thumbnailUrl, Duration runtime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.runtime = runtime;
    }

    public static Theme of(Long id, String name, String description, String thumbnailUrl, Duration runtime) {
        return new Theme(id, name, description, thumbnailUrl, runtime);
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

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public Duration getRuntime() {
        return runtime;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Theme theme = (Theme) o;
        return Objects.equals(id, theme.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
