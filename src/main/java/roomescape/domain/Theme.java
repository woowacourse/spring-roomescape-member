package roomescape.domain;

public class Theme {
    private static final int MAX_LENGTH = 255;

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

    public Theme(String name, String description, String thumbnail) {
        this(null, name, description, thumbnail);
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
            throw new IllegalArgumentException("테마 이름은 비어 있을 수 없습니다. 테마 이름을 입력해주세요.");
        }
        if (name.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("테마 이름은 255자를 넘을 수 없습니다. 255자 이내로 입력해주세요.");
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("테마 설명은 비어 있을 수 없습니다. 테마 설명을 입력해주세요.");
        }
        if (description.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("테마 설명은 255자를 넘을 수 없습니다. 255자 이내로 입력해주세요.");
        }
    }

    private void validateThumbnail(String thumbnail) {
        if (thumbnail == null || thumbnail.isBlank()) {
            throw new IllegalArgumentException("썸네일 경로는 비어 있을 수 없습니다. 썸네일 경로를 입력해주세요.");
        }
        if (thumbnail.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("썸네일 경로는 255자를 넘을 수 없습니다. 255자 이내로 입력해주세요.");
        }
    }
}
