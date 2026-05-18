package roomescape.domain.vo;

import jakarta.validation.constraints.NotBlank;

public record ThemeName(
        String value
) {

    public ThemeName {
        validateNotBlank(value);
    }

    public static ThemeName from(String name) {
        return new ThemeName(name);
    }

    private void validateNotBlank(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("빈 문자열은 테마명으로 사용할 수 없습니다.");
        }
    }
}
