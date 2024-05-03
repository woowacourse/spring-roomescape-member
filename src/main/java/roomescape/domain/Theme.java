package roomescape.domain;

import java.util.Objects;

public class Theme {
    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public Theme(Long id, String name, String description, String thumbnail) {
        validateName(name);
        validateDescription(description);
        validateThumbnail(thumbnail);
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    private void validateName(final String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 테마 이름은 비어있을 수 없습니다.");
        }
    }

    private void validateDescription(final String description) {
        if (description.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 테마 설명은 비어있을 수 없습니다.");
        }
    }

    private void validateThumbnail(final String thumbnail) {
        if (thumbnail.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 테마 썸네일은 비어있을 수 없습니다.");
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
