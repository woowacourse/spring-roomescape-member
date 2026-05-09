package roomescape.theme.domain;

public class Theme {
    private static final String DEFAULT_THUMBNAIL_URL = "https://images.unsplash.com/photo-1505693416388-ac5ce068fe85?auto=format&fit=crop&w=800&q=80";
    private Long id;
    private String name;
    private String description;
    private String thumbnailUrl;
    private boolean isActive;

    private Theme(Long id, String name, String description, String thumbnailUrl, boolean isActive) {
        validate(name, description, thumbnailUrl);
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.isActive = isActive;
    }

    public static Theme create(String name, String description, String thumbnailUrl) {
        return new Theme(null, name, description, resolveThumbnailUrl(thumbnailUrl), false);
    }

    public static Theme load(Long id, String name, String description, String thumbnailUrl, boolean isActive) {
        validateId(id);
        return new Theme(id, name, description, resolveThumbnailUrl(thumbnailUrl), isActive);
    }

    private static void validate(String name, String description, String thumbnailUrl) {
        validateName(name);
        validateDescription(description);
        validateThumbnailUrl(thumbnailUrl);
    }

    private static void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("테마 ID는 필수입니다.");
        }
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("테마 이름은 필수입니다.");
        }
    }

    private static void validateThumbnailUrl(String thumbnailUrl) {
        if (thumbnailUrl == null) {
            throw new IllegalArgumentException("테마 썸네일 URL은 필수입니다.");
        }
    }

    private static void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("테마 설명은 필수입니다.");
        }
    }

    private static String resolveThumbnailUrl(String thumbnailUrl) {
        if (thumbnailUrl.isBlank()) {
            return DEFAULT_THUMBNAIL_URL;
        }
        return thumbnailUrl;
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public String thumbnailUrl() {
        return thumbnailUrl;
    }

    public boolean isActive() {
        return isActive;
    }

    public void updateStatus(boolean isActive) {
        this.isActive = isActive;
    }
}
