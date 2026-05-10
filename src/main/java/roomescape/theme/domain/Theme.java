package roomescape.theme.domain;

public class Theme {

    private static final int MAX_NAME_LENGTH = 255;
    private static final int MAX_DESCRIPTION_LENGTH = 255;
    private static final int MAX_THUMBNAIL_URL_LENGTH = 1024;

    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnailUrl;

    public Theme(Long id, String name, String description, String thumbnailUrl) {
        validateText(name, "이름", MAX_NAME_LENGTH);
        validateText(description, "설명", MAX_DESCRIPTION_LENGTH);
        validateText(thumbnailUrl, "썸네일 URL", MAX_THUMBNAIL_URL_LENGTH);
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
    }

    public static Theme create(String name, String description, String thumbnailUrl) {
        return new Theme(null, name, description, thumbnailUrl);
    }

    public Theme update(String name, String description, String thumbnailUrl) {
        return new Theme(this.id, name, description, thumbnailUrl);
    }

    private void validateText(String value, String fieldName, int maxLength) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(String.format("테마 %s은(는) 비어 있을 수 없습니다.", fieldName));
        }

        if (value.length() > maxLength) {
            throw new IllegalArgumentException(
                    String.format("테마 %s은(는) %d자 이하여야 합니다. (현재 길이: %d)", fieldName, maxLength, value.length())
            );
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
}
