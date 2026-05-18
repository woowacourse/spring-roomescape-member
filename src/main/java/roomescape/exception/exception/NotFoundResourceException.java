package roomescape.exception.exception;

import roomescape.exception.dto.ErrorCode;

public class NotFoundResourceException extends BaseCustomException {
    public NotFoundResourceException(ErrorCode errorCode) {
        super(errorCode);
    }
}
