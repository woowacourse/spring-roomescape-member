package roomescape.global.exception.customException;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.ErrorCode;

public class InvalidRequestException extends BusinessException {

    public InvalidRequestException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus httpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
