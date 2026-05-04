package roomescape.global.exception.customException;

import roomescape.global.exception.ErrorCode;

public class ReservationTimeException extends RuntimeException{

    private final ErrorCode errorCode;

    public ReservationTimeException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
