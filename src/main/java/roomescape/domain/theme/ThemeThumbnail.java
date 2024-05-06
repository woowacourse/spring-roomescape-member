package roomescape.domain.theme;

import roomescape.exception.InvalidValueException;

public class ThemeThumbnail {

    private final String value;

    private ThemeThumbnail(String value) {
        this.value = value;
    }

    public static ThemeThumbnail from(String value) {
        validateNull(value);
        return new ThemeThumbnail(value);
    }

    private static void validateNull(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidValueException("테마 썸네일 URL은 공백일 수 없습니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
