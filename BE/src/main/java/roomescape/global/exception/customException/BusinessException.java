package roomescape.global.exception.customException;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.ErrorCode;

public abstract class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    protected BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    protected BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public abstract HttpStatus httpStatus();
}
