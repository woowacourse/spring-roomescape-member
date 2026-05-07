package roomescape.global.exception.customException;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.ErrorCode;

public class InvalidRequestException extends BusinessException {

    public InvalidRequestException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvalidRequestException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    @Override
    public HttpStatus httpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
