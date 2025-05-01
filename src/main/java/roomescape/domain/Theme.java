package roomescape.domain;

import java.time.LocalTime;
import java.util.Objects;

public class Theme {
    private static final long DEFAULT_ID = 0L;
    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public Theme(final Long id, final String name, final String description, final String thumbnail) {
        validate(id, name, description, thumbnail);
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public static Theme createWithoutId(final String name, final String description, final String thumbnail) {
        return new Theme(DEFAULT_ID, name, description, thumbnail);
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
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Theme theme = (Theme) o;
        return Objects.equals(id, theme.id) && Objects.equals(name, theme.name)
                && Objects.equals(description, theme.description) && Objects.equals(thumbnail,
                theme.thumbnail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, thumbnail);
    }

    private void validate(Long id, String name, String description, String thumbnail) {
        validateNullId(id);
        validateNullName(name);
        validateNullDescription(description);
        validateNullThumbnail(thumbnail);

    }
    private void validateNullId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("[ERROR] 비어있는 ID로 테마를 생성할 수 없습니다.");
        }
    }

    private void validateNullName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 비어있는 이름으로 테마를 생성할 수 없습니다.");
        }
    }

    private void validateNullDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 비어있는 설명으로 테마를 생성할 수 없습니다.");
        }
    }

    private void validateNullThumbnail(String thumbnail) {
        if (thumbnail == null || thumbnail.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 비어있는 썸네일으로 테마를 생성할 수 없습니다.");
        }
    }
}
