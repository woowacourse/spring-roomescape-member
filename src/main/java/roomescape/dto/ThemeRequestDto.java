package roomescape.dto;

import roomescape.domain_entity.Theme;

public record ThemeRequestDto(
        String name, String description, String thumbnail
) {
    public ThemeRequestDto {
        validateNotNull(name, description, thumbnail);
    }

    private void validateNotNull(String name, String description, String thumbnail) {
        if (name == null) {
            throw new IllegalArgumentException("잘못된 name 입력입니다.");
        }
        if (description == null) {
            throw new IllegalArgumentException("잘못된 description 입력입니다.");
        }
        if (thumbnail == null) {
            throw new IllegalArgumentException("잘못된 thumbnail 입력입니다.");
        }
    }

    public Theme toTheme() {
        return new Theme(
                name, description, thumbnail
        );
    }
}
