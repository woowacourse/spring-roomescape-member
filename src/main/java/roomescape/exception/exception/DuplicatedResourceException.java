package roomescape.exception.exception;

import roomescape.exception.dto.ErrorCode;

public class DuplicatedResourceException extends BaseCustomException {
    public DuplicatedResourceException(ErrorCode errorCode) {
        super(errorCode);
    }
}
