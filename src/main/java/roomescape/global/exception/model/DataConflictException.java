package roomescape.global.exception.model;

import roomescape.global.exception.error.ErrorType;

public class DataConflictException extends CustomException {

    public DataConflictException(final ErrorType errorType, final String message) {
        super(errorType, message);
    }
}
