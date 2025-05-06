package roomescape.domain;

import java.util.Objects;
import roomescape.exception.custom.InvalidInputException;

public class RoomTheme {

    private final long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public RoomTheme(final long id, final String name, final String description, final String thumbnail) {
        validate(name, description, thumbnail);

        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public RoomTheme(final String name, final String description, final String thumbnail) {
        this(0, name, description, thumbnail);
    }

    private void validate(final String name, final String description, final String thumbnail) {
        if (Objects.isNull(name) || name.isBlank()) {
            throw new InvalidInputException("테마 명은 빈 값이 입력될 수 없습니다");
        }
        if (Objects.isNull(description) || description.isBlank()) {
            throw new InvalidInputException("테마 상세 설명은 빈 값이 입력될 수 없습니다");
        }
        if (Objects.isNull(thumbnail) || thumbnail.isBlank()) {
            throw new InvalidInputException("섬네일 주소는 빈 값이 입력될 수 없습니다");
        }
    }

    public long getId() {
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
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoomTheme roomTheme = (RoomTheme) o;
        return getId() == roomTheme.getId() && Objects.equals(getName(), roomTheme.getName())
                && Objects.equals(getDescription(), roomTheme.getDescription()) && Objects.equals(
                getThumbnail(), roomTheme.getThumbnail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getThumbnail());
    }
}
