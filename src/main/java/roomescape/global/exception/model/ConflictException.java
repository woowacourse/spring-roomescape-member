package roomescape.global.exception.model;

import roomescape.global.exception.error.ErrorType;

public class ConflictException extends CustomException {

    public ConflictException(final ErrorType errorType, final String message) {
        super(errorType, message);
    }
}
