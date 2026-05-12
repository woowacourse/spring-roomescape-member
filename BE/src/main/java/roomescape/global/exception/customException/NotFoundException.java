package roomescape.global.exception.customException;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.ErrorCode;

public class NotFoundException extends BusinessException {

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus httpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
