package roomescape.domain.theme;

import roomescape.domain.exception.InvalidValueException;

public class ThemeName {

    private final String value;

    private ThemeName(String value) {
        this.value = value;
    }

    public static ThemeName from(String value) {
        validateNull(value);
        return new ThemeName(value);
    }

    private static void validateNull(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidValueException("테마명은 공백일 수 없습니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
