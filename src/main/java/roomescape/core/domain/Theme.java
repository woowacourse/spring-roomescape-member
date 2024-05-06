package roomescape.core.domain;

public class Theme {
    private static final Long DEFAULT_ID = 0L;

    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public Theme(final String name, final String description, final String thumbnail) {
        this(DEFAULT_ID, name, description, thumbnail);
    }

    public Theme(final Long id, final String name, final String description, final String thumbnail) {
        validateEmpty(id, name, description, thumbnail);
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    private void validateEmpty(final Long id, final String name, final String description, final String thumbnail) {
        if (id == null) {
            throw new IllegalArgumentException("테마 id는 null일 수 없습니다.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("테마 이름은 null이나 빈 값일 수 없습니다.");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("테마 설명은 null이나 빈 값일 수 없습니다.");
        }
        if (thumbnail == null || thumbnail.isBlank()) {
            throw new IllegalArgumentException("테마 이미지는 null이나 빈 값일 수 없습니다.");
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
}
