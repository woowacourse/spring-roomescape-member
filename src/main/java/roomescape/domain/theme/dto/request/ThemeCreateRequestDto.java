package roomescape.domain.theme.dto.request;

public record ThemeCreateRequestDto(String name, String description, String imageUrl) {

    public ThemeCreateRequestDto {
        validateName(name);
        validateDescription(description);
        validateImageUrl(imageUrl);
    }

    private static void validateName(String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("테마명은 빈 문자열일 수 없습니다.");
        }
    }

    private static void validateDescription(String description) {
        if (description.isBlank()) {
            throw new IllegalArgumentException("테마 설명은 빈 문자열일 수 없습니다.");
        }
    }

    private static void validateImageUrl(String imageUrl) {
        if (imageUrl.isBlank()) {
            throw new IllegalArgumentException("이미지 url은 빈 문자열일 수 없습니다.");
        }
    }
}
