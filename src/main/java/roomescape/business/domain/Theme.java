package roomescape.business.domain;

public class Theme {

    private Long id;

    private final String name;
    private final String description;

    private final String thumbnail;

    public Theme(
            final String name,
            final String description,
            final String thumbnail
    ) {
        this(null, name, description, thumbnail);
    }

    private Theme(
            final Long id,
            final String name,
            final String description,
            final String thumbnail
    ) {
        validateNonNull(name, description, thumbnail);
        validateNotBlank(name, description, thumbnail);

        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public static Theme createWithId(
            final Long id,
            final String name,
            final String description,
            final String thumbnail
    ) {
        if (id == null) {
            throw new IllegalArgumentException("id가 null 입니다.");
        }

        return new Theme(id, name, description, thumbnail);
    }

    private void validateNonNull(
            final String name,
            final String description,
            final String thumbnail
    ) {
        if (name == null) {
            throw new IllegalArgumentException("name이 null 입니다.");
        }
        if (description == null) {
            throw new IllegalArgumentException("description이 null 입니다.");
        }
        if (thumbnail == null) {
            throw new IllegalArgumentException("thumbnail이 null 입니다.");
        }
    }

    private void validateNotBlank(
            final String name,
            final String description,
            final String thumbnail
    ) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("name이 empty 입니다.");
        }
        if (description.isBlank()) {
            throw new IllegalArgumentException("description이 empty 입니다.");
        }
        if (thumbnail.isBlank()) {
            throw new IllegalArgumentException("thumbnail이 empty 입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}
