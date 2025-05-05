package roomescape.theme.exception;

import org.springframework.http.HttpStatus;
import roomescape.exception.ErrorCode;

public enum InputErrorCode implements ErrorCode {
    INVALID_DATE_RANGE(HttpStatus.BAD_REQUEST, "시작일은 종료일보다 늦을 수 없습니다."),
    ;

    private final HttpStatus status;
    private final String message;

    InputErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }
}
