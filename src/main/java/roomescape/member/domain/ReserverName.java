package roomescape.member.domain;

import roomescape.exception.BadRequestException;
import roomescape.exception.ExceptionCause;

public class ReserverName {
    private final String name;

    public ReserverName(final String name) {
        validateName(name);
        this.name = name;
    }

    private static void validateName(final String name) {
        if (name.isBlank()) {
            throw new BadRequestException(ExceptionCause.EMPTY_VALUE_RESERVATION_NAME);
        }
        if (name.length() > 10) {
            throw new BadRequestException(ExceptionCause.RESERVATION_NAME_INVALID_LENGTH);
        }
    }

    public String getName() {
        return name;
    }
}
