package roomescape.common;

import org.springframework.http.HttpStatus;

public enum ThemeErrorCode implements ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 테마입니다"),
    DUPLICATE_NAME(HttpStatus.CONFLICT, "이미 존재하는 테마 이름입니다"),
    REFERENCED_BY_RESERVATION(HttpStatus.CONFLICT, "예약이 존재하여 테마를 삭제할 수 없습니다");

    private final HttpStatus httpStatus;
    private final String message;

    ThemeErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
