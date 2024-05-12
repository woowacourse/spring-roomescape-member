package roomescape.theme.theme.domain;

import roomescape.global.exception.RoomEscapeException;

import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static roomescape.global.exception.ExceptionMessage.*;

public class Theme {
    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public Theme(Long id, String name, String description, String thumbnail) {
        checkNull(name, description, thumbnail);
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    private void checkNull(String name, String description, String thumbnail) {
        try {
            Objects.requireNonNull(name, THEME_CANNOT_NULL.getMessage());
            Objects.requireNonNull(description, DESCRIPTION_CANNOT_NULL.getMessage());
            Objects.requireNonNull(thumbnail, THUMBNAIL_CANNOT_NULL.getMessage());
        } catch (NullPointerException e) {
            throw new RoomEscapeException(BAD_REQUEST, e.getMessage());
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Theme theme = (Theme) o;
        return Objects.equals(id, theme.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
