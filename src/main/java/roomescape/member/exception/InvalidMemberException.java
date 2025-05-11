package roomescape.member.exception;

import roomescape.global.exception.ValidationException;

public class InvalidMemberException extends ValidationException {

    public InvalidMemberException(final String message) {
        super(message);
    }
}
