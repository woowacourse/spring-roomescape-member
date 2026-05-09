package roomescape.domain.theme.entity;

import java.util.Objects;

public class Theme {

    private Long id;

    private final String name;

    private final String description;

    private final String thumbnailUrl;

    public Theme(Long id, String name, String description, String thumbnailUrl) {
        validateId(id);
        validateName(name);
        validateDescription(description);
        validateThumbnailUrl(thumbnailUrl);

        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
    }

    public static Theme create(String name, String description, String thumbnailUrl) {
        return new Theme(null, name, description, thumbnailUrl);
    }

    public void assignId(Long id) {
        validateAssignableId(id);
        this.id = id;
    }

    private void validateId(Long id) {
        if (id != null && id <= 0) {
            throw new IllegalArgumentException("id는 양수여야 합니다.");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name은 비어 있을 수 없습니다.");
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("description은 비어 있을 수 없습니다.");
        }
    }

    private void validateThumbnailUrl(String thumbnailUrl) {
        if (thumbnailUrl == null || thumbnailUrl.isBlank()) {
            throw new IllegalArgumentException("thumbnailUrl은 비어 있을 수 없습니다.");
        }
    }

    private void validateAssignableId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id는 null일 수 없습니다.");
        }

        validateId(id);

        if (this.id != null) {
            throw new IllegalStateException("이미 id가 할당된 테마입니다.");
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

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        if (this.id == null) {
            return false;
        }
        Theme theme = (Theme) other;
        return Objects.equals(id, theme.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
