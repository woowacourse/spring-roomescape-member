package roomescape.domain;

import roomescape.domain.exception.InvalidDomainObjectException;

import static java.util.Objects.isNull;

public record Description(String value) {
    private static final int DESCRIPTION_LENGTH_MAX = 255;

    public Description {
        if (isNull(value)) {
            throw new InvalidDomainObjectException("description must not be null");
        }
        if (value.length() > DESCRIPTION_LENGTH_MAX) {
            throw new InvalidDomainObjectException(String.format("description must be less than %d characters",
                    DESCRIPTION_LENGTH_MAX));
        }
    }
}
