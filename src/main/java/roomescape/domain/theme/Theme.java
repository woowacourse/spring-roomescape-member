package roomescape.domain.theme;

public class Theme {
    private static final int MAX_DESCRIPTION_LENGTH = 255;
    private static final int MAX_THUMBNAIL_LENGTH = 255;

    private final Long id;
    private final ThemeName name;
    private final String description;
    private final String thumbnail;

    public Theme(Long id, String name, String description, String thumbnail) {
        validateDescriptionLength(description);
        validateThumbnailLength(thumbnail);
        this.id = id;
        this.name = new ThemeName(name);
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Theme(String name, String description, String thumbnail) {
        this(null, name, description, thumbnail);
    }

    private void validateDescriptionLength(String description) {
        if (description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new IllegalArgumentException(String.format("테마 설명은 %d자 이하여야 합니다.", MAX_DESCRIPTION_LENGTH));
        }
    }

    private void validateThumbnailLength(String thumbnail) {
        if (thumbnail.length() > MAX_THUMBNAIL_LENGTH) {
            throw new IllegalArgumentException(String.format("썸네일 URL은 %d자 이하여야 합니다.", MAX_THUMBNAIL_LENGTH));
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
