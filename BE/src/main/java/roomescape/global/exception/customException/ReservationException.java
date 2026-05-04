package roomescape.global.exception.customException;

import roomescape.global.exception.ErrorCode;

public class ReservationException extends RuntimeException {

    private final ErrorCode errorCode;

    public ReservationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
