package roomescape.service.dto.theme;

import roomescape.global.exception.InvalidThemeException;

public record CreateThemeCommand(
        String name,
        String description,
        String imageUrl
) {

    private static final int MAX_VARCHAR_LENGTH = 255;
    private static final int MAX_URL_LENGTH = 512;

    public CreateThemeCommand {
        validateName(name);
        validateDescription(description);
        validateImageUrl(imageUrl);
        name = name.trim();
        description = description.trim();
        imageUrl = imageUrl.trim();
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidThemeException("테마 이름은 필수입니다.");
        }
        if (name.length() > MAX_VARCHAR_LENGTH) {
            throw new InvalidThemeException("테마 이름은 " + MAX_VARCHAR_LENGTH + "자를 초과할 수 없습니다.");
        }
    }

    private static void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new InvalidThemeException("테마 설명은 필수입니다.");
        }
        if (description.length() > MAX_VARCHAR_LENGTH) {
            throw new InvalidThemeException("테마 설명은 " + MAX_VARCHAR_LENGTH + "자를 초과할 수 없습니다.");
        }
    }

    private static void validateImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new InvalidThemeException("테마 이미지 URL은 필수입니다.");
        }
        if (imageUrl.length() > MAX_URL_LENGTH) {
            throw new InvalidThemeException("이미지 URL은 " + MAX_URL_LENGTH + "자를 초과할 수 없습니다.");
        }
    }
}
