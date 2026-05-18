package roomescape.global.exception.customException;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.ErrorCode;

public abstract class BusinessException extends RuntimeException {

    protected BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }

    public abstract HttpStatus httpStatus();
}
