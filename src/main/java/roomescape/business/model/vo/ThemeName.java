package roomescape.business.model.vo;

import roomescape.exception.business.InvalidCreateArgumentException;

public record ThemeName(
        String value
) {
    private static final int MAX_LENGTH = 20;

    public ThemeName {
        validateMaxLength(value);
    }

    private static void validateMaxLength(final String name) {
        if (name.length() > MAX_LENGTH) {
            throw new InvalidCreateArgumentException("테마 이름은 %d자를 넘길 수 없습니다.".formatted(MAX_LENGTH));
        }
    }
}
