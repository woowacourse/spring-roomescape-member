package roomescape.domain.global.exception;

import java.util.Collections;
import java.util.List;

public class BadRequestException extends RuntimeException implements BaseException {

    private final ErrorCode errorCode;
    private final List<ErrorDetail> errors;

    public BadRequestException(ErrorCode errorCode, List<ErrorDetail> errors) {
        this.errorCode = errorCode;
        this.errors = errors;
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public List<ErrorDetail> getErrors() {
        return Collections.unmodifiableList(errors);
    }

}
