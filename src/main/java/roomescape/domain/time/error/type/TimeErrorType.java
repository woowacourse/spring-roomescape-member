package roomescape.domain.time.error.type;

import org.springframework.http.HttpStatus;
import roomescape.global.error.type.ErrorType;

public enum TimeErrorType implements ErrorType {
    FIELD_RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "조회할 자원이 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    TimeErrorType(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public HttpStatus status() {
        return httpStatus;
    }

    @Override
    public String message() {
        return message;
    }
}
