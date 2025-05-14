package roomescape.domain;

import java.util.Objects;

public class Theme {

    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public Theme(Long id, String name, String description, String thumbnail) {
        validate(name, description, thumbnail);
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public static Theme createWithoutId(String name, String description, String thumbnail) {
        return new Theme(null, name, description, thumbnail);
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
        return Objects.equals(id, theme.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    private void validate(String name, String description, String thumbnail) {
        validateNullName(name);
        validateNullDescription(description);
        validateNullThumbnail(thumbnail);

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
