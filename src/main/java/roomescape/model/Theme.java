package roomescape.model;

public class Theme {

    private final Long id;
    private final ThemeName name;
    private final String description;
    private final String thumbnail;

    public Theme(final String name, final String description, final String thumbnail) {
        this(null, name, description, thumbnail);
    }

    public Theme(final Long id, final String name, final String description, final String thumbnail) {
        validateDescription(description);
        validateThumbnail(thumbnail);
        this.id = id;
        this.name = new ThemeName(name);
        this.description = description;
        this.thumbnail = thumbnail;
    }

    private void validateDescription(final String description) {
        if (description.isBlank()) {
            throw new IllegalArgumentException("테마 설명이 비어 있습니다.");
        }
    }

    private void validateThumbnail(final String thumbnail) {
        if (thumbnail.isBlank()) {
            throw new IllegalArgumentException("테마 썸네일이 비어 있습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
