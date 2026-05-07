package roomescape.domain;

public record Theme(Long id, String name, String description, String thumbnailUrl) {

    public Theme {
        validateName(name);
        validateDescription(description);
        validateThumbnailUrl(thumbnailUrl);
    }

    public static Theme transientOf(String name, String description, String thumbnailUrl) {
        return new Theme(
                null,
                name,
                description,
                thumbnailUrl
        );
    }

    private void validateThumbnailUrl(String thumbnailUrl) {
        if (thumbnailUrl == null) {
            throw new IllegalArgumentException("썸네일 URL은 필수입니다.");
        }
    }

    private void validateDescription(String description) {
        if (description == null) {
            throw new IllegalArgumentException("테마 설명은 필수입니다.");
        }
    }

    private void validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("테마 이름은 필수입니다.");
        }
    }
}
