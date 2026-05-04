package roomescape.controller.dto;

public record ThemeRequest(
        String name,
        String description,
        String thumbnail
) {

    public ThemeRequest {
        validateName(name);
        validateDescription(description);
        validateThumbnail(thumbnail);
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 테마 이름은 비어 있을 수 없습니다.");
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("[ERROR] 테마 이름은 255자를 넘을 수 없습니다.");
        }
    }

    private void validateDescription(String description) {
        if (description != null && description.length() > 255) {
            throw new IllegalArgumentException("[ERROR] 테마 설명은 255자를 넘을 수 없습니다.");
        }
    }

    private void validateThumbnail(String thumbnail) {
        if (thumbnail != null && thumbnail.length() > 255) {
            throw new IllegalArgumentException("[ERROR] 썸네일 경로는 255자를 넘을 수 없습니다.");
        }
    }
}
