package roomescape.domain.theme;

import roomescape.domain.exception.InvalidDomainObjectException;

import static java.util.Objects.isNull;

public record ThemeName(String value) {
    private static final int NAME_LENGTH_MIN = 2;
    private static final int NAME_LENGTH_MAX = 20;

    public ThemeName {
        if (isNull(value)) {
            throw new InvalidDomainObjectException("theme name must not be null");
        }
        if (NAME_LENGTH_MAX < value.length() || value.length() < NAME_LENGTH_MIN) {
            throw new InvalidDomainObjectException(String.format("테마 이름은 %d자 이상 %d자 이하여야 합니다. (현재 입력한 이름 길이: %d자)",
                    NAME_LENGTH_MIN, NAME_LENGTH_MAX, value.length()));
        }
    }
}
