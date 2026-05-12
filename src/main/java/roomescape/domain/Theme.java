package roomescape.domain;

public class Theme {

    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnailUrl;

    public Theme(Long id, String name, String description, String thumbnailUrl) {
        validate(name, description, thumbnailUrl);

        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
    }

    public Theme(String name, String description, String thumbnailUrl) {
        validate(name, description, thumbnailUrl);

        this.id = null;
        this.name = name;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
    }

    public static Theme of(Long id, Theme theme) {
        return new Theme(id, theme.name, theme.description, theme.thumbnailUrl);
    }

    private void validate(String name, String description, String thumbnailUrl) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 이름은 비어 있을 수 없습니다.");
        }
        if (description.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 설명은 비어 있을 수 없습니다.");
        }
        if (thumbnailUrl.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 썸네일은 비어 있을 수 없습니다.");
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
