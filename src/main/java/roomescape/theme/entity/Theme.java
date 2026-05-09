package roomescape.theme.entity;

import java.time.Duration;
import java.util.Objects;

public class Theme {

    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnailUrl;
    private final Duration runtime;

    private Theme(String name, String description, String thumbnailUrl, Duration runtime) {
        this(null, name, description, thumbnailUrl, runtime);
    }

    private Theme(Long id, String name, String description, String thumbnailUrl, Duration runtime) {
        validate(name, description, thumbnailUrl, runtime);
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.runtime = runtime;
    }

    private void validate(String name, String description, String thumbnailUrl, Duration runtime) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("테마 이름은 필수입니다.");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("테마 설명은 필수입니다.");
        }
        if (thumbnailUrl == null || thumbnailUrl.isBlank()) {
            throw new IllegalArgumentException("테마 썸네일은 필수입니다.");
        }
        if (runtime == null) {
            throw new IllegalArgumentException("테마 진행 시간은 필수입니다.");
        }
    }

    public static Theme of(String name, String description, String thumbnailUrl, Duration runtime) {
        return new Theme(name, description, thumbnailUrl, runtime);
    }

    public static Theme of(Long id, String name, String description, String thumbnailUrl, Duration runtime) {
        return new Theme(id, name, description, thumbnailUrl, runtime);
    }

    public static Theme toEntity(Theme theme, Long id) {
        return new Theme(id, theme.name, theme.description, theme.thumbnailUrl, theme.runtime);
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
