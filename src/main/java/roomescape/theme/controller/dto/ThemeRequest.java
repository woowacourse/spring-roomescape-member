package roomescape.theme.controller.dto;

import jakarta.validation.constraints.NotBlank;
import roomescape.theme.domain.Theme;

public record ThemeRequest(
        @NotBlank String name,
        @NotBlank String description,
        @NotBlank String thumbnail
) {

    public ThemeRequest {
        validateName(name);
        validateDescription(description);
        validateThumbnail(thumbnail);
    }

    public Theme toThemeWithoutId() {
        return new Theme(null, name, description, thumbnail);
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름은 필수입니다.");
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("설명은 필수입니다.");
        }
    }

    private void validateThumbnail(String thumbnail) {
        if (thumbnail == null || thumbnail.isBlank()) {
            throw new IllegalArgumentException("대표 이미지는 필수입니다.");
        }
    }

}
