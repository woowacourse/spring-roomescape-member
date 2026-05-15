package roomescape.domain.global.exception.custom;

import java.util.Collections;
import java.util.List;
import roomescape.domain.global.exception.error.ErrorCode;
import roomescape.domain.global.exception.error.ErrorDetail;

public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;
    private final List<ErrorDetail> errors;

    public BusinessException(ErrorCode errorCode, List<ErrorDetail> errors) {
        this.errorCode = errorCode;
        this.errors = errors;
    }

    public BusinessException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errors = Collections.emptyList();
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public List<ErrorDetail> getErrors() {
        return Collections.unmodifiableList(errors);
    }
}
