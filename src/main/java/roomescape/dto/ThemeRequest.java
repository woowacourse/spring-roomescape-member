package roomescape.dto;

import roomescape.domain_entity.Theme;

public record ThemeRequest(
        String name, String description, String thumbnail
) {
    public ThemeRequest {
        validateNotNull(name, description, thumbnail);
    }

    private void validateNotNull(String name, String description, String thumbnail) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("잘못된 name 입력입니다.");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("잘못된 description 입력입니다.");
        }
        if (thumbnail == null || thumbnail.isBlank()) {
            throw new IllegalArgumentException("잘못된 thumbnail 입력입니다.");
        }
    }

    public Theme toTheme() {
        return new Theme(
                name, description, thumbnail
        );
    }
}
