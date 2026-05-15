package roomescape.exception;

import org.springframework.http.HttpStatus;

public enum ThemeErrorCode implements ErrorCode {
    THEME_DUPLICATE(HttpStatus.CONFLICT, "같은 테마가 존재합니다."),
    THEME_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 테마입니다."),
    THEME_INVALID_NAME(HttpStatus.BAD_REQUEST, "테마 이름의 형식이 올바르지 않습니다."),
    THEME_INVALID_DESCRIPTION(HttpStatus.BAD_REQUEST, "테마 설명의 형식이 올바르지 않습니다."),
    THEME_INVALID_URL(HttpStatus.BAD_REQUEST, "테마 이미지 URL 형식이 올바르지 않습니다.");

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
    public String getErrorMessage() {
        return message;
    }

    @Override
    public String getErrorName() {
        return this.name();
    }
}
