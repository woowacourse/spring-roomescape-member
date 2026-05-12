package roomescape.domain;

public class Theme {

    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnailImageUrl;

    public Theme(Long id, String name, String description, String thumbnailImageUrl) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("테마 이름은 필수입니다.");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("테마 설명은 필수입니다.");
        }
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnailImageUrl = thumbnailImageUrl;
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

    public String getThumbnailImageUrl() {
        return thumbnailImageUrl;
    }
}
