package roomescape.domain;

import roomescape.exception.EmptyValueException;
import roomescape.exception.ExceptionCause;

public class ReserverName {
    private final String name;

    public ReserverName(final String name) {
        validateName(name);
        this.name = name;
    }

    private static void validateName(final String name) {
        if (name.isBlank()) {
            throw new EmptyValueException(ExceptionCause.EMPTY_VALUE_RESERVATION_NAME);
        }
        if (name.length() > 10) {
            throw new IllegalArgumentException("예약자명은 10자 이하여야합니다.");
        }
    }

    public String getName() {
        return name;
    }
}
