package roomescape.theme.theme.domain;

import roomescape.global.exception.RoomEscapeException;

import java.util.Objects;

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
            Objects.requireNonNull(name, "[ERROR] 테마 이름은 null일 수 없습니다.");
            Objects.requireNonNull(description, "[ERROR] 설명은 null일 수 없습니다.");
            Objects.requireNonNull(thumbnail, "[ERROR] 썸네일은 null일 수 없습니다.");
        } catch (NullPointerException e) {
            throw new RoomEscapeException(e.getMessage());
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
