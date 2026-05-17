package roomescape.domain;

public class Theme {

    private static final long DEFAULT_RUNNING_TIME = 60L;

    private final Long id;
    private final String name;
    private final String description;
    private final String imageUrl;
    private final Long runningTime;

    public Theme(Long id, String name, String description, String imageUrl) {
        validate(name, description, imageUrl);
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.runningTime = DEFAULT_RUNNING_TIME;
    }

    public static Theme withoutId(String name, String description, String imageUrl) {
        return new Theme(null, name, description, imageUrl);
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

    public String getImageUrl() {
        return imageUrl;
    }

    public Long getRunningTime() {
        return runningTime;
    }

    @Override
    public String toString() {
        return "Theme{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    private void validate(String name, String description, String imageUrl) {
        validateName(name);
        validateDescription(description);
        validateImageUrl(imageUrl);
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("테마 이름은 필수입니다.");
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("테마 설명은 필수입니다.");
        }
    }

    private void validateImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IllegalArgumentException("테마 이미지는 필수입니다.");
        }
    }
}
