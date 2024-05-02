package roomescape.domain.dto;

import roomescape.domain.Theme;

public record ThemeRequest(String name, String description, String thumbnail) {
    public ThemeRequest {
        isValid(name, description, thumbnail);
    }

    private void isValid(final String name, final String description, final String thumbnail) {
        validEmpty(name);
        validEmpty(description);
        validEmpty(thumbnail);
    }

    private void validEmpty(final String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 테마 등록 시 빈 값은 허용하지 않습니다");
        }
    }

    public Theme toEntity(final Long id) {
        return new Theme(id, name, description, thumbnail);
    }
}
