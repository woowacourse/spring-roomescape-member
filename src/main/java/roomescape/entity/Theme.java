package roomescape.entity;

public record Theme(Long id, String name, String description, String thumbnail) {

    public Theme {
        validate(name, description, thumbnail);
    }

    public boolean equalsThemeName(Theme theme) {
        return this.name.equals(theme.name);
    }

    private void validate(String name, String description, String thumbnail) {
        validateName(name);
        validateDescription(description);
        validateThumbnail(thumbnail);
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 테마명입니다.");
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 테마 설명입니다.");
        }
    }

    private void validateThumbnail(String thumbnail) {
        if (thumbnail == null || thumbnail.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 테마 썸네일 주소입니다.");
        }
    }
}
