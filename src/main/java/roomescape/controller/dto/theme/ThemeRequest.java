package roomescape.controller.dto.theme;

import roomescape.global.exception.InvalidThemeException;
import roomescape.service.dto.CreateThemeCommand;

public record ThemeRequest(
        String name,
        String description,
        String imageUrl
) {

    public CreateThemeCommand toCommand() {
        validateName();
        validateDescription();
        validateImageUrl();

        return new CreateThemeCommand(
                name.trim(),
                description.trim(),
                imageUrl.trim()
        );
    }

    private void validateName() {
        if (name == null || name.isBlank()) {
            throw new InvalidThemeException("테마 이름은 필수입니다.");
        }
    }

    private void validateDescription() {
        if (description == null || description.isBlank()) {
            throw new InvalidThemeException("테마 설명은 필수입니다.");
        }
    }

    private void validateImageUrl() {
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new InvalidThemeException("테마 이미지 URL은 필수입니다.");
        }
    }
}
