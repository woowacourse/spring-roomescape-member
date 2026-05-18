package roomescape.global.exception.customException;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.ErrorCode;

public class ForbiddenException extends BusinessException {
    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus httpStatus() {
        return HttpStatus.FORBIDDEN;
    }
}
