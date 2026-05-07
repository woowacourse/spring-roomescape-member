package roomescape.controller.dto;

public record ThemeRequest(String name, String description, String thumbnailUrl) {

    public ThemeRequest {
        validate(name, description, thumbnailUrl);
    }

    private void validate(String name, String description, String thumbnailUrl) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("테마 이름은 필수이며 비어있을 수 없습니다.");
        }
        if (description == null) {
            throw new IllegalArgumentException("설명은 필수입니다.");
        }
        if (thumbnailUrl == null) {
            throw new IllegalArgumentException("썸네일은 필수입니다.");
        }
    }
}
