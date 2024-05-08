package roomescape.domain.theme;

import roomescape.domain.exception.InvalidDomainObjectException;

import static java.util.Objects.isNull;

public record Description(String value) {
    private static final int DESCRIPTION_LENGTH_MAX = 255;

    public Description {
        if (isNull(value)) {
            throw new InvalidDomainObjectException("description must not be null");
        }
        if (value.length() > DESCRIPTION_LENGTH_MAX) {
            throw new InvalidDomainObjectException(String.format("설명은 %d자 이하여야 합니다. (현재 입력한 설명 길이: %d자)",
                    DESCRIPTION_LENGTH_MAX, value.length()));
        }
    }
}
