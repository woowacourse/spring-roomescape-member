package roomescape.exception.exception;

import roomescape.exception.dto.ErrorCode;

public class DataReferencedException extends BaseCustomException {
    public DataReferencedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
