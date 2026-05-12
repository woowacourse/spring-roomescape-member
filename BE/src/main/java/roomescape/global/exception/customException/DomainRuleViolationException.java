package roomescape.global.exception.customException;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.ErrorCode;

public class DomainRuleViolationException extends BusinessException {

    public DomainRuleViolationException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus httpStatus() {
        return HttpStatus.UNPROCESSABLE_ENTITY;
    }
}
