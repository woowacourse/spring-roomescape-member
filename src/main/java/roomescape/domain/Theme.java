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

    private void validateName(String name) {
        if (name == null || name.isBlank() || name.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 테마의 이름은 1글자 이상으로 이루어져야 합니다.");
        }
    }

    private void validateDescription(String description) {
        if (description == null) {
            throw new IllegalArgumentException("[ERROR] 테마 설명이 없습니다.");
        }
    }

    private void validateThumbnail(String thumbnail) {
        if (thumbnail == null) {
            throw new IllegalArgumentException("[ERROR] 테마 이미지가 없습니다.");
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
