package roomescape.global.exception;

import lombok.Getter;

@Getter
public abstract class ReservationException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String detail;

    protected ReservationException(ErrorCode errorCode, String message, String detail) {
        super(message);
        this.errorCode = errorCode;
        this.detail = detail;
    }

    protected ReservationException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.detail = null;
    }
}
