package roomescape.domain.theme;

import roomescape.domain.exception.InvalidDomainObjectException;

import static java.util.Objects.isNull;

public record ThemeName(String value) {
    private static final int NAME_LENGTH_MIN = 2;
    private static final int NAME_LENGTH_MAX = 20;

    public ThemeName {
        if (isNull(value)) {
            throw new InvalidDomainObjectException("value must not be null");
        }
        if (NAME_LENGTH_MAX < value.length() || value.length() < NAME_LENGTH_MIN) {
            throw new InvalidDomainObjectException(String.format("value must be between %d and %d characters",
                    NAME_LENGTH_MIN, NAME_LENGTH_MAX));
        }
    }
}
