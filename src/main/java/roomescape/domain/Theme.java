package roomescape.domain;

public class Theme {

    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public Theme(Long id, String name, String description, String thumbnail) {
        validateName(name);
        validateDescription(description);
        validateThumbnail(thumbnail);

        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
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

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 테마 이름은 비어 있을 수 없습니다.");
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("[ERROR] 테마 이름은 255자를 넘을 수 없습니다.");
        }
    }

    private void validateDescription(String name) {
        if (name.length() > 255) {
            throw new IllegalArgumentException("[ERROR] 테마 설명은 255자를 넘을 수 없습니다.");
        }
    }

    private void validateThumbnail(String name) {
        if (name.length() > 255) {
            throw new IllegalArgumentException("[ERROR] 썸네일 경로는 255자를 넘을 수 없습니다.");
        }
    }
}
